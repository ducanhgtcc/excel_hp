package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mobile.plus.request.attendance.*;
import com.example.onekids_project.mobile.plus.response.attendanceKids.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AttendanceKidsPlusService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.attendancekids.AttendanceConfigResponse;
import com.example.onekids_project.response.attendancekids.StatusAttendanceDay;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.attendancekids.AttendanceKidsService;
import com.example.onekids_project.util.AttendanceKidsUtil;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AttendanceKidsPlusServiceImpl implements AttendanceKidsPlusService {


    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AttendanceArriveKidsRepository attendanceArriveKidsRepository;

    @Autowired
    private AttendanceLeaveKidsRepository attendanceLeaveKidsRepository;

    @Autowired
    private AttendanceEatKidsRepository attendanceEatKidsRepository;

    @Autowired
    private AttendanceConfigRepository attendanceConfigRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private AttendanceKidsService attendanceKidsService;

    @Override
    public AttendanceKidResponse searchAttendanceGrade(UserPrincipal principal, SearchAttendanceGradeRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate date = request.getDate();
        List<MaClass> maClassList = maClassRepository.findClassInGradeWithDate(idSchool, request.getIdGrade(), date);
        List<Long> idClassList = maClassList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<Kids> kidsList = kidsRepository.findKidInClassAndStatusWithDate(date, idClassList);
        if (CollectionUtils.isEmpty(kidsList)) {
            return new AttendanceKidResponse();
        }
        AttendanceKidResponse response = this.getAttendaceKidList(kidsList, date);

        return response;
    }

    @Override
    public List<AttendanceKidClassResponse> searchAttendanceClass(UserPrincipal principal, AttendanceGradePlusRequest request) {
        LocalDate date = request.getDate();
        List<MaClass> maClassList = maClassRepository.findClassInGradeWithDate(principal.getIdSchoolLogin(), request.getIdGrade(), date);
        List<AttendanceKidClassResponse> responseList = new ArrayList<>();
        maClassList.forEach(y -> {
            AttendanceKidClassResponse data = new AttendanceKidClassResponse();
            List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(date, y.getId());
            AttendanceKidResponse attendaceResponse = this.getAttendaceKidList(kidsList, date);
            data.setClassName(y.getClassName());
            data.setId(y.getId());
            data.setAttendanceKidResponse(attendaceResponse);
            responseList.add(data);
        });
        return responseList;
    }

    @Override
    public StatisticAttendanceArriveKid searchAttendanceArrive(UserPrincipal principal, AttendanceClassPlusRequest request) {
        StatisticAttendanceArriveKid response = new StatisticAttendanceArriveKid();
        LocalDate date = request.getDate();
        Long idClass = request.getIdClass();
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(date, idClass);
        if (CollectionUtils.isEmpty(kidsList)) {
            return response;
        }
//        CheckStatusKidResponse checkStatusKidResponse = this.checkStatusKid(kids);
        AttendanceKidResponse attendaceResponse = this.getAttendaceKidList(kidsList, date);
        response.setNumberAbsent(attendaceResponse.getNumberAbsentYes() + attendaceResponse.getNumberAbsentNo());
        response.setNumberKidNow(attendaceResponse.getNumberKidNow());
        response.setNumberAttendaceNo(attendaceResponse.getNumberAttendaceNo());
        response.setNumberAttendaceYes(attendaceResponse.getNumberAttendaceYes());

        return response;
    }

    @Override
    public List<AttendanceArriveKidClassResponse> searchAttendanceArriveKid(UserPrincipal principal, AttendanceClassPlusRequest request) {
        List<AttendanceArriveKidClassResponse> responseList = new ArrayList<>();
        LocalDate date = request.getDate();
        Long idClass = request.getIdClass();
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(date, idClass);
        if (CollectionUtils.isEmpty(kidsList)) {
            return responseList;
        }
        List<Long> idKidList = kidsList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsDateList(idKidList, date);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return responseList;
        }
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKidsList.get(0).getAttendanceConfig(), date);
        attendanceKidsList.forEach(x -> {
            AttendanceArriveKidClassResponse response = this.getArriveKids(attendanceConfigResponse, x);
            if (response != null) {
                responseList.add(response);
            }
        });
        return responseList;
    }

    @Override
    public List<AttendanceArriveKidClassResponse> createAttendanceArriveKid(UserPrincipal principal, AttendanceArrivePlusRequest request) throws IOException, FirebaseMessagingException {
        List<AttendanceArriveKidClassResponse> responseList = new ArrayList<>();
        Long id = request.getIdList().get(0);
        AttendanceKids attendanceKids = attendanceKidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        LocalDate date = attendanceKids.getAttendanceDate();
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKidsRepository.findByIdAndDelActiveTrue(id).orElseThrow().getAttendanceConfig(), date);

        if (!attendanceConfigResponse.isAfternoonAttendanceArrive() && !attendanceConfigResponse.isEveningAttendanceArrive() && !attendanceConfigResponse.isMorningAttendanceArrive()) {
            return responseList;
        }
        for (Long x : request.getIdList()) {
            AttendanceKids attendanceKidNew = attendanceKidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            AttendanceArriveKids attendanceArriveKidNew = attendanceKidNew.getAttendanceArriveKids();
            boolean checkFireBase = attendanceArriveKidNew.getIdCreated() == null;
            attendanceArriveKidNew.setArriveContent(request.getContent());
            if (request.getPicture() != null) {
                HandleFileUtils.deletePictureInFolder(attendanceArriveKidNew.getArriveUrlPictureLocal());
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                attendanceArriveKidNew.setArriveUrlPicture(handleFileResponse.getUrlWeb());
                attendanceArriveKidNew.setArriveUrlPictureLocal(handleFileResponse.getUrlLocal());
            } else if (request.isDeletePicture()) {
                HandleFileUtils.deletePictureInFolder(attendanceArriveKidNew.getArriveUrlPictureLocal());
                attendanceArriveKidNew.setArriveUrlPicture(null);
                attendanceArriveKidNew.setArriveUrlPictureLocal(null);
            }
            this.setPropertiesArrive(attendanceConfigResponse, request, attendanceArriveKidNew);
            if (attendanceArriveKidNew.isMorning() || attendanceArriveKidNew.isAfternoon() || attendanceArriveKidNew.isEvening()) {
                attendanceArriveKidNew.setTimeArriveKid(attendanceArriveKidNew.getTimeArriveKid() == null ? LocalTime.now() : request.getTime());
            } else {
                attendanceArriveKidNew.setTimeArriveKid(null);
            }

            boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_ARRIVE, principal, attendanceKidNew);
            attendanceKidNew.setAttendanceArrive(AttendanceKidsUtil.checkArriveHas(attendanceArriveKidNew));
            AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKidNew);
            //tự động điểm danh ăn theo config
            attendanceKidsService.saveAttendanceEatAuto(attendanceKidsSaved);
            AttendanceArriveKidClassResponse response = this.getArriveKids(attendanceConfigResponse, attendanceKidsSaved);
            if (response != null) {
                responseList.add(response);
            }
            if (checkSendFirebase) {
                long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.SCHOOL);
                if (idWebSystem != 0) {
                    //send firebase
                    firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
                }
            }
        }

        return responseList;
    }


    @Transactional
    @Override
    public List<AttendanceArriveKidClassResponse> createAttendanceArriveKidMulti(UserPrincipal principal, ArriveMultiRequest requestList) throws IOException, FirebaseMessagingException {
        List<AttendanceArriveKidClassResponse> responseList = new ArrayList<>();
        List<AttendanceArriveMultiPlusRequest> requests = requestList.getArriveMultiRequest();
        Long id = requests.get(0).getId();
        AttendanceKids attendanceKids = attendanceKidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        LocalDate date = attendanceKids.getAttendanceDate();
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKidsRepository.findByIdAndDelActiveTrue(id).orElseThrow().getAttendanceConfig(), date);
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            return responseList;
        }
        for (AttendanceArriveMultiPlusRequest request : requests) {
            AttendanceKids attendanceKidNew = attendanceKidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
            AttendanceArriveKids attendanceArriveKidNew = attendanceKidNew.getAttendanceArriveKids();
            attendanceArriveKidNew.setArriveContent(request.getContent());
            boolean checkFireBase = attendanceArriveKidNew.getIdCreated() == null;
            if (request.getPicture() != null) {
                HandleFileUtils.deletePictureInFolder(attendanceArriveKidNew.getArriveUrlPictureLocal());
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                attendanceArriveKidNew.setArriveUrlPicture(handleFileResponse.getUrlWeb());
                attendanceArriveKidNew.setArriveUrlPictureLocal(handleFileResponse.getUrlLocal());
            } else if (request.isDeletePicture()) {
                HandleFileUtils.deletePictureInFolder(attendanceArriveKidNew.getArriveUrlPictureLocal());
                attendanceArriveKidNew.setArriveUrlPicture(null);
                attendanceArriveKidNew.setArriveUrlPictureLocal(null);
            }
            this.setPropertiesArriveMulti(attendanceConfigResponse, request, attendanceArriveKidNew);
            if (attendanceArriveKidNew.isMorning() || attendanceArriveKidNew.isAfternoon() || attendanceArriveKidNew.isEvening()) {
                attendanceArriveKidNew.setTimeArriveKid(attendanceArriveKidNew.getTimeArriveKid() == null ? LocalTime.now() : request.getTime());
            } else {
                attendanceArriveKidNew.setTimeArriveKid(null);
            }
            boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_ARRIVE, principal, attendanceKidNew);
            attendanceKidNew.setAttendanceArriveKids(attendanceArriveKidNew);
            attendanceKidNew.setAttendanceArrive(AttendanceKidsUtil.checkArriveHas(attendanceArriveKidNew));
            AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKidNew);
            //tự động điểm danh ăn theo config
            attendanceKidsService.saveAttendanceEatAuto(attendanceKidsSaved);
            //du liệu trả về
            AttendanceArriveKidClassResponse response = this.getArriveKids(attendanceConfigResponse, attendanceKidsSaved);
            if (response != null) {
                responseList.add(response);
            }

            if (checkSendFirebase) {
                long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.SCHOOL);
                if (idWebSystem != 0) {
                    //send firebase
                    firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
                }
            }
        }
        return responseList;
    }

    @Transactional
    @Override
    public AiAttendanceArriveKidClassResponse createAttendanceArriveKidAi(UserPrincipal principal, AiAttendanceKidArrivePlusRequest requests) throws IOException, FirebaseMessagingException {
        Long idKid = requests.getId();
        Long idClass = requests.getIdClass();
        LocalDate date = LocalDate.now();
        AiAttendanceArriveKidClassResponse response = new AiAttendanceArriveKidClassResponse();
        AttendanceKids attendanceKids = attendanceKidsRepository.findByDelActiveTrueAndAttendanceDateAndKids_Id(date, idKid);
        if (attendanceKids == null) {
            response.setStatus(AppConstant.APP_FALSE);
            return response;
        }
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.getAttendanceConfig(), date);
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            response.setStatus(AppConstant.APP_FALSE);
            return response;
        }
        AttendanceArriveKids attendanceArriveKids = attendanceKids.getAttendanceArriveKids();
        boolean checkFireBase = attendanceArriveKids.getIdCreated() == null;
        attendanceArriveKids.setArriveContent(requests.getContent());
        if (requests.getPicture() != null) {
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(requests.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
            String urlWeb = handleFileResponse.getUrlWeb();
            String urlLocal = handleFileResponse.getUrlLocal();
            attendanceArriveKids.setArriveUrlPicture(urlWeb);
            attendanceArriveKids.setArriveUrlPictureLocal(urlLocal);
        }
        attendanceArriveKids.setTimeArriveKid(LocalTime.now());
        this.setPropertiesArriveAi(attendanceConfigResponse, requests, attendanceArriveKids);
        if (attendanceArriveKids.isMorning() || attendanceArriveKids.isAfternoon() || attendanceArriveKids.isEvening()) {
            attendanceArriveKids.setTimeArriveKid(LocalTime.now());
        } else {
            attendanceArriveKids.setTimeArriveKid(null);
        }

        boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_ARRIVE, principal, attendanceKids);
        attendanceKids.setAttendanceArrive(AttendanceKidsUtil.checkArriveHas(attendanceArriveKids));
        AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKids);
        response.setStatus(AppConstant.APP_TRUE);
        //tự động điểm danh ăn theo config
        attendanceKidsService.saveAttendanceEatAuto(attendanceKidsSaved);

        if (checkSendFirebase) {
            long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.SCHOOL);
            if (idWebSystem != 0) {
                //send firebase
                firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
            }
        }
        return response;
    }

    @Override
    public StatisticAttendanceLeaveKid searchAttendanceLeave(UserPrincipal principal, AttendanceClassPlusRequest request) {
        StatisticAttendanceLeaveKid response = new StatisticAttendanceLeaveKid();

        LocalDate date = request.getDate();
        Long idClass = request.getIdClass();
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(date, idClass);
        if (CollectionUtils.isEmpty(kidsList)) {
            return response;
        }
//        CheckStatusKidResponse checkStatusKidResponse = this.checkStatusKid(kids);
        AttendanceKidResponse attendaceResponse = this.getAttendaceKidList(kidsList, date);
        response.setNumberAbsent(attendaceResponse.getNumberAbsentYes() + attendaceResponse.getNumberAbsentNo());
        response.setNumberKidNow(attendaceResponse.getNumberKidNow());
        response.setNumberAttendaceNo(attendaceResponse.getNumberLeaveNo());
        response.setNumberAttendaceYes(attendaceResponse.getNumberLeaveYes());

        return response;
    }

    @Override
    public List<AttendanceLeaveKidClassResponse> searchAttendanceLeaveKid(UserPrincipal principal, AttendanceClassPlusRequest request) {
        List<AttendanceLeaveKidClassResponse> responseList = new ArrayList<>();
        LocalDate date = request.getDate();
        Long idClass = request.getIdClass();
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(date, idClass);
        if (CollectionUtils.isEmpty(kidsList)) {
            return responseList;
        }
        List<Long> idKidList = kidsList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsDateList(idKidList, date);
//        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByKidsInAndAttendanceDate(kidsList, date).stream().sorted(Comparator.comparing(x -> x.getKids().getFirstName())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return responseList;
        }
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKidsList.get(0).getAttendanceConfig(), date);
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            return responseList;
        } else {
            attendanceKidsList.forEach(x -> {
                AttendanceLeaveKids attendanceLeaveKids = x.getAttendanceLeaveKids();
                //du liệu trả về
                AttendanceLeaveKidClassResponse response = this.reponseAttendanceLeave(attendanceLeaveKids, x);
                responseList.add(response);
            });
        }
        return responseList;
    }

    @Transactional
    @Override
    public List<AttendanceLeaveKidClassResponse> createAttendanceLeaveKid(UserPrincipal principal, AttendanceLeavePlusRequest request) throws IOException, FirebaseMessagingException {
        Long id = request.getIdList().get(0);
        AttendanceKids attendanceKid = attendanceKidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKid.getAttendanceConfig(), attendanceKid.getAttendanceDate());
        List<AttendanceLeaveKidClassResponse> responseList = new ArrayList<>();
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            return responseList;
        } else {
            for (Long x : request.getIdList()) {
                AttendanceKids attendanceKidNew = attendanceKidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
                AttendanceLeaveKids attendanceLeaveKidNew = attendanceKidNew.getAttendanceLeaveKids();
                boolean checkFireBase = attendanceLeaveKidNew.getIdCreated() == null;
                attendanceLeaveKidNew.setLeaveContent(request.getContent());
                attendanceLeaveKidNew.setStatusLeave(AppConstant.APP_TRUE);
                if (request.getPicture() != null) {
                    HandleFileUtils.deletePictureInFolder(attendanceLeaveKidNew.getLeaveUrlPictureLocal());
                    HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                    attendanceLeaveKidNew.setLeaveUrlPicture(handleFileResponse.getUrlWeb());
                    attendanceLeaveKidNew.setLeaveUrlPictureLocal(handleFileResponse.getUrlLocal());
                } else if (request.isDeletePicture()) {
                    HandleFileUtils.deletePictureInFolder(attendanceLeaveKidNew.getLeaveUrlPictureLocal());
                    attendanceLeaveKidNew.setLeaveUrlPicture(null);
                    attendanceLeaveKidNew.setLeaveUrlPictureLocal(null);
                }
                if (request.getTime() != null) {
                    attendanceLeaveKidNew.setTimeLeaveKid(request.getTime());
                    attendanceLeaveKidNew.setMinutePickupLate(this.setTimePickup(request.getTime(), principal.getSchoolConfig().getTimePickupKid()));
                } else if (attendanceLeaveKidNew.getTimeLeaveKid() == null) {
                    attendanceLeaveKidNew.setTimeLeaveKid(LocalTime.now());
                    attendanceLeaveKidNew.setMinutePickupLate(this.setTimePickup(LocalTime.now(), principal.getSchoolConfig().getTimePickupKid()));
                }
                AttendanceLeaveKids attendanceLeaveKids = attendanceLeaveKidsRepository.save(attendanceLeaveKidNew);
                attendanceKidNew.setAttendanceLeave(AppConstant.APP_TRUE);
                boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_LEAVE, principal, attendanceKidNew);
                attendanceKidNew.setAttendanceLeaveKids(attendanceLeaveKids);
                AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKidNew);
                //du liệu trả về
                AttendanceLeaveKidClassResponse response = this.reponseAttendanceLeave(attendanceLeaveKids, attendanceKidNew);
                responseList.add(response);
                if (checkSendFirebase) {
                    long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.SCHOOL);
                    if (idWebSystem != 0) {
                        //send firebase
                        firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
                    }
                }
            }

            return responseList;
        }

    }

    @Transactional
    @Override
    public List<AttendanceLeaveKidClassResponse> createAttendanceLeaveKidMulti(UserPrincipal principal, LeaveMultiRequest requestList) throws IOException, FirebaseMessagingException {
        List<AttendanceLeaveMultiPlusRequest> requests = requestList.getLeaveMultiRequest();
        Long id = requests.get(0).getId();
        AttendanceKids attendanceKid = attendanceKidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKid.getAttendanceConfig(), attendanceKid.getAttendanceDate());
        List<AttendanceLeaveKidClassResponse> responseList = new ArrayList<>();
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            return responseList;
        } else {
            for (AttendanceLeaveMultiPlusRequest x : requests) {
                AttendanceKids attendanceKidNew = attendanceKidsRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
                AttendanceLeaveKids attendanceLeaveKidNew = attendanceKidNew.getAttendanceLeaveKids();
                boolean checkFireBase = attendanceLeaveKidNew.getIdCreated() == null;
                attendanceLeaveKidNew.setLeaveContent(x.getContent());
                attendanceLeaveKidNew.setStatusLeave(AppConstant.APP_TRUE);
                if (x.getPicture() != null) {
                    HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(x.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                    String urlWeb = handleFileResponse.getUrlWeb();
                    String urlLocal = handleFileResponse.getUrlLocal();
                    attendanceLeaveKidNew.setLeaveUrlPicture(urlWeb);
                    attendanceLeaveKidNew.setLeaveUrlPictureLocal(urlLocal);
                } else if (x.isDeletePicture()) {
                    HandleFileUtils.deletePictureInFolder(attendanceLeaveKidNew.getLeaveUrlPictureLocal());
                    attendanceLeaveKidNew.setLeaveUrlPicture(null);
                    attendanceLeaveKidNew.setLeaveUrlPictureLocal(null);
                }
                if (x.getTime() != null) {
                    attendanceLeaveKidNew.setTimeLeaveKid(x.getTime());
                    attendanceLeaveKidNew.setMinutePickupLate(this.setTimePickup(x.getTime(), principal.getSchoolConfig().getTimePickupKid()));
                } else if (attendanceLeaveKidNew.getTimeLeaveKid() == null) {
                    attendanceLeaveKidNew.setTimeLeaveKid(LocalTime.now());
                    attendanceLeaveKidNew.setMinutePickupLate(this.setTimePickup(LocalTime.now(), principal.getSchoolConfig().getTimePickupKid()));
                }
                AttendanceLeaveKids attendanceLeaveKids = attendanceLeaveKidsRepository.save(attendanceLeaveKidNew);
                attendanceKidNew.setAttendanceLeave(AppConstant.APP_TRUE);
                attendanceKidNew.setAttendanceLeaveKids(attendanceLeaveKids);
                boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_LEAVE, principal, attendanceKidNew);
                AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKidNew);

                //du liệu trả về
                AttendanceLeaveKidClassResponse response = this.reponseAttendanceLeave(attendanceLeaveKids, attendanceKidNew);
                responseList.add(response);
                if (checkSendFirebase) {
                    long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.SCHOOL);
                    if (idWebSystem != 0) {
                        //send firebase
                        firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
                    }
                }
            }
            return responseList;
        }
    }

    @Transactional
    @Override
    public AiAttendanceLeaveKidClassResponse createAttendanceLeaveKidAi(UserPrincipal principal, AiAttendanceKidLeavePlusRequest requests) throws IOException, FirebaseMessagingException {
        Long idKid = requests.getId();
        Long idClass = requests.getIdClass();
        LocalDate date = LocalDate.now();
        AiAttendanceLeaveKidClassResponse response = new AiAttendanceLeaveKidClassResponse();
        AttendanceKids attendanceKids = attendanceKidsRepository.findByDelActiveTrueAndAttendanceDateAndKids_Id(date, idKid);
        if (attendanceKids == null) {
            response.setStatus(AppConstant.APP_FALSE);
            return response;
        }
        AttendanceLeaveKids attendanceLeaveKids = attendanceKids.getAttendanceLeaveKids();
        boolean checkFireBase = attendanceLeaveKids.getIdCreated() == null;
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.getAttendanceConfig(), date);
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            response.setStatus(AppConstant.APP_FALSE);
            return response;
        }
        attendanceLeaveKids.setLeaveContent(requests.getContent());
        attendanceLeaveKids.setStatusLeave(AppConstant.APP_TRUE);
        if (requests.getPicture() != null) {
            HandleFileUtils.deletePictureInFolder(attendanceLeaveKids.getLeaveUrlPictureLocal());
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(requests.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
            attendanceLeaveKids.setLeaveUrlPicture(handleFileResponse.getUrlWeb());
            attendanceLeaveKids.setLeaveUrlPictureLocal(handleFileResponse.getUrlLocal());
        }
        attendanceLeaveKids.setTimeLeaveKid(LocalTime.now());
        attendanceLeaveKids.setMinutePickupLate(this.setTimePickup(LocalTime.now(), principal.getSchoolConfig().getTimePickupKid()));
        AttendanceLeaveKids attendanceLeaveKidsSave = attendanceLeaveKidsRepository.save(attendanceLeaveKids);
        attendanceKids.setAttendanceLeave(AppConstant.APP_TRUE);
        attendanceKids.setAttendanceLeaveKids(attendanceLeaveKidsSave);
        boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_LEAVE, principal, attendanceKids);
        AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKids);
        response.setStatus(AppConstant.APP_TRUE);

        if (checkSendFirebase) {
            long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.SCHOOL);
            if (idWebSystem != 0) {
                //send firebase
                firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
            }
        }
        return response;
    }

    @Override
    public StatisticAttendanceEatKid searchAttendanceEat(UserPrincipal principal, AttendanceClassPlusRequest request) {
        StatisticAttendanceEatKid response = new StatisticAttendanceEatKid();
        LocalDate date = request.getDate();
        Long idClass = request.getIdClass();
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(date, idClass);
        if (CollectionUtils.isEmpty(kidsList)) {
            return new StatisticAttendanceEatKid();
        }
        AttendanceKidResponse attendaceResponse = this.getAttendaceKidList(kidsList, date);
        response.setNumberAbsent(attendaceResponse.getNumberAbsentYes() + attendaceResponse.getNumberAbsentNo());
        response.setNumberKidNow(attendaceResponse.getNumberKidNow());
        response.setNumberAttendaceNo(attendaceResponse.getNumberEatNo());
        response.setNumberAttendaceYes(attendaceResponse.getNumberEatYes());
        return response;
    }

    @Override
    public List<AttendanceEatKidClassResponse> searchAttendanceEatKid(UserPrincipal principal, AttendanceClassPlusRequest request) {
        List<AttendanceEatKidClassResponse> responseList = new ArrayList<>();
        LocalDate date = request.getDate();
        Long idClass = request.getIdClass();
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(date, idClass);
        if (CollectionUtils.isEmpty(kidsList)) {
            return responseList;
        }
        List<Long> idKidList = kidsList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsDateList(idKidList, date);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return responseList;
        }
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKidsList.get(0).getAttendanceConfig(), date);
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            return responseList;
        }
        attendanceKidsList.forEach(x -> {
            AttendanceEatKidClassResponse response = this.reponseAttendaceEat(attendanceConfigResponse, x);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<AttendanceEatKidClassResponse> createAttendanceEatKid(UserPrincipal principal, AttendanceEatPlusRequest request) {
        Long id = request.getIdList().get(0);
        AttendanceKids attendanceKid = attendanceKidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKid.getAttendanceConfig(), attendanceKid.getAttendanceDate());
        List<AttendanceEatKidClassResponse> responseList = new ArrayList<>();
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            return responseList;
        }
        request.getIdList().forEach(x -> {
            AttendanceKids attendanceKidNew = attendanceKidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            this.setAttendaceEat(attendanceKidNew, attendanceConfigResponse, request);
            AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_EAT, principal, attendanceKidNew);
            attendanceKidsRepository.save(attendanceKidNew);
            //du liệu trả về
            AttendanceEatKidClassResponse response = this.reponseAttendaceEat(attendanceConfigResponse, attendanceKidNew);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<AttendanceEatKidClassResponse> createAttendanceEatKidMulti(UserPrincipal principal, EatMultiRequest requestList) {
        List<AttendanceEatMultiPlusRequest> request = requestList.getEatMultiRequest();
        Long id = request.get(0).getId();
        AttendanceKids attendanceKid = attendanceKidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKid.getAttendanceConfig(), attendanceKid.getAttendanceDate());
        List<AttendanceEatKidClassResponse> responseList = new ArrayList<>();
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            return responseList;
        }
        request.forEach(x -> {
            AttendanceKids attendanceKidNew = attendanceKidsRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
            this.setAttendaceEatMulti(attendanceKidNew, attendanceConfigResponse, x);
            AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_EAT, principal, attendanceKidNew);
            attendanceKidsRepository.save(attendanceKidNew);

            //du liệu trả về
            AttendanceEatKidClassResponse response = this.reponseAttendaceEat(attendanceConfigResponse, attendanceKidNew);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public StatusAttendanceDay checkAttendanceStatusDay(UserPrincipal principal, Long idClass) {
        StatusAttendanceDay response = new StatusAttendanceDay();
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(LocalDate.now(), idClass);
        if (CollectionUtils.isEmpty(kidsList)) {
            return response;
        }
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findByKidsInAndAttendanceDate(kidsList, LocalDate.now());


        if (!CollectionUtils.isEmpty(attendanceKids)) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), LocalDate.now());
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return response;
            }
            int arriveFull = 0;
            int arriveEmplty = 0;
            int leaveFull = 0;
            int leaveEmplty = 0;
            int eatFull = 0;
            int eatEmplty = 0;

            for (AttendanceKids x : attendanceKids) {
                if (x.isAttendanceArrive()) {
                    arriveFull++;
                } else {
                    arriveEmplty++;
                }
                if (x.isAttendanceLeave()) {
                    leaveFull++;
                } else {
                    leaveEmplty++;
                }
                if (x.isAttendanceEat()) {
                    eatFull++;
                } else {
                    eatEmplty++;
                }
            }
            if (attendanceKids.size() == arriveFull) {
                response.setArrive(AttendanceConstant.FULL);
            } else if (attendanceKids.size() == arriveEmplty) {
                response.setArrive(AttendanceConstant.EMPTY);
            } else {
                response.setArrive(AttendanceConstant.PART);
            }
            if (attendanceKids.size() == leaveFull) {
                response.setLeave(AttendanceConstant.FULL);
            } else if (attendanceKids.size() == leaveEmplty) {
                response.setLeave(AttendanceConstant.EMPTY);
            } else {
                response.setLeave(AttendanceConstant.PART);
            }
            if (attendanceKids.size() == eatFull) {
                response.setEat(AttendanceConstant.FULL);
            } else if (attendanceKids.size() == eatEmplty) {
                response.setEat(AttendanceConstant.EMPTY);
            } else {
                response.setEat(AttendanceConstant.PART);
            }
        }
        return response;
    }

    private void setAttendaceEatMulti(AttendanceKids attendanceKidNew, AttendanceConfigResponse attendanceConfigResponse, AttendanceEatMultiPlusRequest x) {
        AttendanceEatKids attendanceEatKids = attendanceKidNew.getAttendanceEatKids();
        attendanceKidNew.setAttendanceEat(AppConstant.APP_TRUE);
        if (attendanceConfigResponse.isMorningAttendanceArrive()) {
            if (attendanceConfigResponse.isMorningEat()) {
                attendanceEatKids.setBreakfast(x.isMorning());
            }
            if (attendanceConfigResponse.isSecondMorningEat()) {
                attendanceEatKids.setSecondBreakfast(x.isSecondMorning());
            }
        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isMorningAttendanceArrive()) {
            if (attendanceConfigResponse.isLunchEat()) {
                attendanceEatKids.setLunch(x.isLunch());
            }
        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
            if (attendanceConfigResponse.isAfternoonEat()) {
                attendanceEatKids.setAfternoon(x.isAfternoon());
            }
            if (attendanceConfigResponse.isSecondAfternoonEat()) {
                attendanceEatKids.setSecondAfternoon(x.isSecondAfternoon());

            }
        }
        if (attendanceConfigResponse.isEveningAttendanceArrive()) {
            if (attendanceConfigResponse.isEveningEat()) {
                attendanceEatKids.setDinner(x.isDinner());
            }
        }
        attendanceKidNew.setAttendanceEatKids(attendanceEatKids);
    }

    private AttendanceEatKidClassResponse reponseAttendaceEat(AttendanceConfigResponse attendanceConfigResponse, AttendanceKids x) {
        AttendanceEatKidClassResponse response = new AttendanceEatKidClassResponse();
        AttendanceEatKids eatKids = x.getAttendanceEatKids();
        response.setId(x.getId());
        response.setNameKid(x.getKids().getFullName());
        response.setNickName(StringUtils.isNotBlank(x.getKids().getNickName()) ? x.getKids().getNickName() : "");
        response.setStatusEat(AttendanceKidsUtil.checkEat(eatKids));
        if (x.isAttendanceArrive()) {
            if (x.getAttendanceArriveKids().isMorning() || x.getAttendanceArriveKids().isAfternoon() || x.getAttendanceArriveKids().isEvening()) {
                response.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
            } else response.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
        } else {
            response.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
        }
        response.setAvatar(ConvertData.getAvatarKid(x.getKids()));
        if (attendanceConfigResponse.isMorningAttendanceArrive()) {
            if (attendanceConfigResponse.isMorningEat()) {
                response.setMorning(eatKids.isBreakfast());
            }
            if (attendanceConfigResponse.isSecondMorningEat()) {
                response.setSecondMorning(eatKids.isSecondBreakfast());
            }
        }
        if (attendanceConfigResponse.isMorningAttendanceArrive() || attendanceConfigResponse.isAfternoonAttendanceArrive()) {
            if (attendanceConfigResponse.isLunchEat()) {
                response.setLunch(eatKids.isLunch());
            }
        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
            if (attendanceConfigResponse.isAfternoonEat()) {
                response.setAfternoon(eatKids.isAfternoon());
            }
            if (attendanceConfigResponse.isSecondAfternoonEat()) {
                response.setSecondAfternoon(eatKids.isSecondAfternoon());
            }
        }
        if (attendanceConfigResponse.isEveningAttendanceArrive()) {
            if (attendanceConfigResponse.isEveningEat()) {
                response.setDinner(eatKids.isDinner());
            }
        }
        return response;
    }

    private CheckStatusKidResponse checkStatusKid(List<Kids> kids) {
        int numberKidNow = (int) kids.stream().filter(x -> x.getKidStatus().equalsIgnoreCase(KidsStatusConstant.STUDYING)).count();
//        long numberReserve = kids.stream().filter(x -> x.getKidStatus().equalsIgnoreCase(KidsStatusConstant.RESERVE)).count();
//        long numberAbsent = kids.stream().filter(x -> x.getKidStatus().equalsIgnoreCase(KidsStatusConstant.LEAVE_SCHOOL)).count();

        CheckStatusKidResponse data = new CheckStatusKidResponse();
//        data.setNumberAbsent(String.valueOf(numberReserve));
        data.setNumberKidNow(numberKidNow);
//        data.setNumberReserve(String.valueOf(numberAbsent));

        return data;
    }

    private AttendanceKidResponse getAttendaceKidList(List<Kids> kidsList, LocalDate date) {
        AttendanceKidResponse response = new AttendanceKidResponse();
        if (CollectionUtils.isEmpty(kidsList)) {
            return response;
        }
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByKidsInAndAttendanceDate(kidsList, date);
        AtomicInteger eatYes = new AtomicInteger();
        AtomicInteger eatNo = new AtomicInteger();
        AtomicInteger attendaceYes = new AtomicInteger();
        AtomicInteger attendaceNo = new AtomicInteger();
        AtomicInteger attendaceLeaveYes = new AtomicInteger();
        AtomicInteger attendaceLeaveNo = new AtomicInteger();
        AtomicInteger attendaceAbsentYes = new AtomicInteger();
        AtomicInteger attendaceAbsentNo = new AtomicInteger();

        attendanceKidsList.forEach(x -> {
            if (x.isAttendanceArrive()) {
                attendaceYes.addAndGet(1);
            } else {
                attendaceNo.addAndGet(1);
            }
            if (x.isAttendanceEat()) {
                eatYes.getAndIncrement();
            } else {
                eatNo.getAndIncrement();
            }
            if (x.isAttendanceLeave()) {
                attendaceLeaveYes.addAndGet(1);
            } else {
                attendaceLeaveNo.addAndGet(1);
            }
            if (x.getAttendanceArriveKids().isMorningYes() && x.getAttendanceArriveKids().isAfternoonYes()) {
                attendaceAbsentYes.addAndGet(1);
            }
            if (x.getAttendanceArriveKids().isMorningNo() && x.getAttendanceArriveKids().isAfternoonNo()) {
                attendaceAbsentNo.addAndGet(1);
            }
        });

        response.setNumberKidNow(kidsList.size());
        response.setNumberEatNo(eatNo.get());
        response.setNumberEatYes(eatYes.get());
        response.setNumberAttendaceNo(attendaceNo.get());
        response.setNumberAttendaceYes(attendaceYes.get());
        response.setNumberLeaveYes(attendaceLeaveYes.get());
        response.setNumberLeaveNo(attendaceLeaveNo.get());
        response.setNumberAbsentYes(attendaceAbsentYes.get());
        response.setNumberAbsentNo(attendaceAbsentNo.get());
        response.setNumberArrive(kidsList.size() - (attendaceAbsentYes.get() + attendaceAbsentNo.get() + attendaceNo.get()));
        return response;
    }

    /**
     * sét trạng thái điểm danh ăn
     *
     * @param attendanceConfigResponse
     * @param request
     * @param
     */
    private AttendanceKids setAttendaceEat(AttendanceKids attendanceKidNew, AttendanceConfigResponse attendanceConfigResponse, AttendanceEatPlusRequest request) {
        AttendanceEatKids attendanceEatKids = attendanceKidNew.getAttendanceEatKids();
        attendanceKidNew.setAttendanceEat(AppConstant.APP_TRUE);
        if (attendanceConfigResponse.isMorningAttendanceArrive()) {
            if (attendanceConfigResponse.isMorningEat()) {
                attendanceEatKids.setBreakfast(request.isMorning());
            }
            if (attendanceConfigResponse.isSecondMorningEat()) {
                attendanceEatKids.setSecondBreakfast(request.isSecondMorning());
            }
        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isMorningAttendanceArrive()) {
            if (attendanceConfigResponse.isLunchEat()) {
                attendanceEatKids.setLunch(request.isLunch());
            }
        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
            if (attendanceConfigResponse.isAfternoonEat()) {
                attendanceEatKids.setAfternoon(request.isAfternoon());
            }
            if (attendanceConfigResponse.isSecondAfternoonEat()) {
                attendanceEatKids.setSecondAfternoon(request.isSecondAfternoon());

            }
        }
        if (attendanceConfigResponse.isEveningAttendanceArrive()) {
            if (attendanceConfigResponse.isEveningEat()) {
                attendanceEatKids.setDinner(request.isDinner());
            }
        }
        attendanceKidNew.setAttendanceEatKids(attendanceEatKids);
        return attendanceKidNew;
    }

    /**
     * sét trạng thái điểm danh
     *
     * @param attendanceConfigResponse
     * @param request
     * @param attendanceArriveKidNew
     */
    private void setPropertiesArrive(AttendanceConfigResponse attendanceConfigResponse, AttendanceArrivePlusRequest request, AttendanceArriveKids attendanceArriveKidNew) {
        if (attendanceConfigResponse.isMorningAttendanceArrive() && request.getMoring() != null) {
            attendanceArriveKidNew.setMorning(request.getMoring().isGoSchool());
            attendanceArriveKidNew.setMorningYes(request.getMoring().isAbsentYes());
            attendanceArriveKidNew.setMorningNo(request.getMoring().isAbsentNo());
        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive() && request.getAfternoon() != null) {
            attendanceArriveKidNew.setAfternoon(request.getAfternoon().isGoSchool());
            attendanceArriveKidNew.setAfternoonYes(request.getAfternoon().isAbsentYes());
            attendanceArriveKidNew.setAfternoonNo(request.getAfternoon().isAbsentNo());
        }
        if (attendanceConfigResponse.isEveningAttendanceArrive() && request.getEvening() != null) {
            attendanceArriveKidNew.setEvening(request.getEvening().isGoSchool());
            attendanceArriveKidNew.setEveningYes(request.getEvening().isAbsentNo());
            attendanceArriveKidNew.setEveningNo(request.getEvening().isAbsentYes());
        }
    }

    /**
     * sét trạng thái điểm danh multi
     *
     * @param attendanceConfigResponse
     * @param request
     * @param attendanceArriveKidNew
     */
    private void setPropertiesArriveMulti(AttendanceConfigResponse attendanceConfigResponse, AttendanceArriveMultiPlusRequest request, AttendanceArriveKids attendanceArriveKidNew) {
        if (attendanceConfigResponse.isMorningAttendanceArrive() && request.getMoring() != null) {
            attendanceArriveKidNew.setMorning(request.getMoring().isGoSchool());
            attendanceArriveKidNew.setMorningYes(request.getMoring().isAbsentYes());
            attendanceArriveKidNew.setMorningNo(request.getMoring().isAbsentNo());
        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive() && request.getAfternoon() != null) {
            attendanceArriveKidNew.setAfternoon(request.getAfternoon().isGoSchool());
            attendanceArriveKidNew.setAfternoonYes(request.getAfternoon().isAbsentYes());
            attendanceArriveKidNew.setAfternoonNo(request.getAfternoon().isAbsentNo());
        }
        if (attendanceConfigResponse.isEveningAttendanceArrive() && request.getEvening() != null) {
            attendanceArriveKidNew.setEvening(request.getEvening().isGoSchool());
            attendanceArriveKidNew.setEveningYes(request.getEvening().isAbsentNo());
            attendanceArriveKidNew.setEveningNo(request.getEvening().isAbsentYes());
        }
    }

    /**
     * sét trạng thái điểm danh multi
     *
     * @param attendanceConfigResponse
     * @param
     * @param
     */
    private void setPropertiesArriveAi(AttendanceConfigResponse attendanceConfigResponse, AiAttendanceKidArrivePlusRequest requests, AttendanceArriveKids attendanceArriveKids) {
        if (attendanceConfigResponse.isMorningAttendanceArrive() && requests.getMorningList() != null) {
            attendanceArriveKids.setMorning(requests.getMorningList().isGoSchool());
            attendanceArriveKids.setMorningYes(requests.getMorningList().isAbsentYes());
            attendanceArriveKids.setMorningNo(requests.getMorningList().isAbsentNo());
        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive() && requests.getAfternoonList() != null) {
            attendanceArriveKids.setAfternoon(requests.getAfternoonList().isGoSchool());
            attendanceArriveKids.setAfternoonYes(requests.getAfternoonList().isAbsentYes());
            attendanceArriveKids.setAfternoonNo(requests.getAfternoonList().isAbsentNo());
        }
        if (attendanceConfigResponse.isEveningAttendanceArrive() && requests.getEveningList() != null) {
            attendanceArriveKids.setEvening(requests.getEveningList().isGoSchool());
            attendanceArriveKids.setEveningYes(requests.getEveningList().isAbsentYes());
            attendanceArriveKids.setEveningNo(requests.getEveningList().isAbsentNo());
        }
    }

    /**
     * dữ liệu trả về attendance leave
     *
     * @param attendanceLeaveKids
     * @param attendanceKidNew
     * @return
     */
    private AttendanceLeaveKidClassResponse reponseAttendanceLeave(AttendanceLeaveKids attendanceLeaveKids, AttendanceKids attendanceKidNew) {
        AttendanceLeaveKidClassResponse response = new AttendanceLeaveKidClassResponse();
        response.setTime(attendanceLeaveKids.getTimeLeaveKid() != null ? ConvertData.convertTimeHHMM(attendanceLeaveKids.getTimeLeaveKid()) : "");
        response.setPicture(Strings.isNotBlank(attendanceLeaveKids.getLeaveUrlPicture()) ? attendanceLeaveKids.getLeaveUrlPicture() : "");
        response.setAvatar(ConvertData.getAvatarKid(attendanceKidNew.getKids()));
        response.setIdKid(attendanceKidNew.getKids().getId());
        if (attendanceKidNew.isAttendanceArrive()) {
            if (attendanceKidNew.getAttendanceArriveKids().isMorning() || attendanceKidNew.getAttendanceArriveKids().isAfternoon() || attendanceKidNew.getAttendanceArriveKids().isEvening()) {
                response.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
            } else response.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
        } else {
            response.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
        }
        response.setStatusLeave(attendanceKidNew.isAttendanceLeave());
        response.setNameKid(attendanceKidNew.getKids().getFullName());
        response.setNickName(StringUtils.isNotBlank(attendanceKidNew.getKids().getNickName()) ? attendanceKidNew.getKids().getNickName() : "");
        response.setContent(attendanceLeaveKids.getLeaveContent());
        response.setId(attendanceKidNew.getId());
        return response;
    }

    // lấy số phut đi học muộn
    private int setTimePickup(LocalTime timeRq, LocalTime timePickUp) {
        long timeCv = ConvertData.convertLocalTimeToLong(timeRq);
        long timeCf = ConvertData.convertLocalTimeToLong(timePickUp);
        if (timeCv > timeCf) {
            return (int) (timeCv - timeCf);
        } else {
            return 0;
        }
    }

    /**
     * lấy dữ liệu trả về
     *
     * @param attendanceConfigResponse
     * @param x
     * @return
     */
    private AttendanceArriveKidClassResponse getArriveKids(AttendanceConfigResponse attendanceConfigResponse, AttendanceKids x) {
        AttendanceArriveKidClassResponse model = new AttendanceArriveKidClassResponse();
        AttendanceArriveKids arriveKids = x.getAttendanceArriveKids();
        model.setId(x.getId());
        model.setIdKid(x.getKids().getId());
        model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
        model.setNameKid(x.getKids().getFullName());
        model.setNickName(StringUtils.isNotBlank(x.getKids().getNickName()) ? x.getKids().getNickName() : "");

        model.setContent(Strings.isBlank(arriveKids.getArriveContent()) ? "" : arriveKids.getArriveContent());
        model.setPicture(Strings.isBlank(arriveKids.getArriveUrlPicture()) ? "" : arriveKids.getArriveUrlPicture());
        model.setTime(arriveKids.getTimeArriveKid() == null ? "" : ConvertData.convertTimeHHMM(arriveKids.getTimeArriveKid()));

        boolean check = false;
        if (attendanceConfigResponse.isMorningAttendanceArrive()) {
            model.setStatus(x.isAttendanceArrive());
            AttendanceStatusDayPlusResponse morring = new AttendanceStatusDayPlusResponse();
            morring.setGoSchool(arriveKids.isMorning());
            morring.setAbsentYes(arriveKids.isMorningYes());
            morring.setAbsentNo(arriveKids.isMorningNo());
            model.setMorning(morring);
            check = true;

        }
        if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
            model.setStatus(x.isAttendanceArrive());
            AttendanceStatusDayPlusResponse affternoon = new AttendanceStatusDayPlusResponse();
            affternoon.setGoSchool(arriveKids.isAfternoon());
            affternoon.setAbsentYes(arriveKids.isAfternoonYes());
            affternoon.setAbsentNo(arriveKids.isAfternoonNo());
            model.setAffternoon(affternoon);
            check = true;

        }
        if (attendanceConfigResponse.isEveningAttendanceArrive()) {
            model.setStatus(x.isAttendanceArrive());
            AttendanceStatusDayPlusResponse evening = new AttendanceStatusDayPlusResponse();
            evening.setGoSchool(arriveKids.isEvening());
            evening.setAbsentYes(arriveKids.isEveningYes());
            evening.setAbsentNo(arriveKids.isEveningNo());
            model.setEvening(evening);
            check = true;
        }
        if (check) {
            return model;
        }
        return null;
    }
}



