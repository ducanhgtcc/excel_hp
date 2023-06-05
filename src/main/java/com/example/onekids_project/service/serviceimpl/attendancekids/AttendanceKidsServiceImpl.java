package com.example.onekids_project.service.serviceimpl.attendancekids;

import com.example.onekids_project.common.*;
import com.example.onekids_project.dto.ListIdKidDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.importexport.model.AttendanceKidsModel;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.attendancekids.*;
import com.example.onekids_project.response.attendancekids.*;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.service.servicecustom.attendancekids.AttendanceKidsService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * điểm danh cho học sinh
 */
@Service
public class AttendanceKidsServiceImpl implements AttendanceKidsService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private AttendanceArriveKidsRepository attendanceArriveKidsRepository;

    @Autowired
    private AttendanceLeaveKidsRepository attendanceLeaveKidsRepository;

    @Autowired
    private AttendanceConfigRepository attendanceConfigRepository;

    @Autowired
    private AbsentLetterRepository absentLetterRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private MaClassService maClassService;
    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public List<Long> checkAbsentHas(UserPrincipal principal, AbsentCheckSearchRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        List<AbsentLetter> absentLetterList = absentLetterRepository.findAbsentInClassDate(principal.getIdSchoolLogin(), request.getIdClass(), request.getDate());
        List<Long> longList = absentLetterList.stream().map(x -> x.getId()).collect(Collectors.toList());
        return longList;
    }

    /**
     * tìm kiếm điểm danh cho các học sinh
     *
     * @param principal
     * @param attendanceKidsSearchRequest
     * @return
     */
    @Override
    public ListAttendanceKidsDetailDateResponse searchAllAttendanceKidsDetailDate(UserPrincipal principal, AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListAttendanceKidsDetailDateResponse data = new ListAttendanceKidsDetailDateResponse();
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.searchAllAttendanceKidsDate(idSchool, AttendanceConstant.ATTENDANCE_ARRIVE, attendanceKidsSearchRequest);
        AttendanceConfigResponse attendanceConfigResponse = this.getAttendanceConfig(idSchool, attendanceKidsList, attendanceKidsSearchRequest.getDate());
        List<AttendanceKidsDetailDateResponse> attendanceKidsDetailDateResponseList = listMapper.mapList(attendanceKidsList, AttendanceKidsDetailDateResponse.class);
        data.setAttendanceDetailDateList(attendanceKidsDetailDateResponseList);
        data.setAttendanceConfigResponse(attendanceConfigResponse);
        return data;
    }

    @Override
    public ListAttendanceArriveKidsDateResponse searchAttendanceArriveKidsDate(UserPrincipal principal, Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        ListAttendanceArriveKidsDateResponse data = new ListAttendanceArriveKidsDateResponse();
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.searchAllAttendanceKidsDate(idSchool, AttendanceConstant.ATTENDANCE_ARRIVE, attendanceKidsSearchRequest);
        AttendanceConfigResponse attendanceConfigResponse = this.getAttendanceConfig(idSchool, attendanceKidsList, attendanceKidsSearchRequest.getDate());
        List<AttendanceArriveKidsDateResponse> attendanceArriveKidsDateResponseList = listMapper.mapList(attendanceKidsList, AttendanceArriveKidsDateResponse.class);
        data.setAttendanceConfigResponse(attendanceConfigResponse);
        data.setAttendanceDateList(attendanceArriveKidsDateResponseList);
        return data;
    }


    @Transactional
    @Override
    public AttendanceArriveKidsDateResponse saveAttendanceArriveOneKidsDate(Long idSchool, UserPrincipal principal, AttendanceArriveKidsDateRequest attendanceArriveKidsDateRequest) throws FirebaseMessagingException {
        Optional<AttendanceKids> attendanceKidsOptional = attendanceKidsRepository.findByIdAttendance(idSchool, attendanceArriveKidsDateRequest.getId());
        AttendanceKids attendanceKids = attendanceKidsOptional.get();
        modelMapper.map(attendanceArriveKidsDateRequest, attendanceKids);
        //set auditing cho điểm danh đến
        boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_ARRIVE, principal, attendanceKids);
        attendanceKids.setAttendanceArrive(this.getAttendanceArriveStastus(attendanceArriveKidsDateRequest.getAttendanceArriveKids()));
        this.resetAttendanceLeaveKids(attendanceKids);
        AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKids);
        //tự động điểm danh ăn theo config
        this.saveAttendanceEatAuto(attendanceKidsSaved);
        AttendanceArriveKidsDateResponse attendanceArriveKidsDateResponse = modelMapper.map(attendanceKidsSaved, AttendanceArriveKidsDateResponse.class);
        if (checkSendFirebase) {
            long idWebSystem = 0;
            if (AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
                idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(principal.getAppType())) {
                idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.TEACHER);
            }
            if (idWebSystem != 0) {
                //send firebase
                firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
            }
        }
        return attendanceArriveKidsDateResponse;
    }

    @Override
    public boolean updateContentArrive(Long idSchool, UserPrincipal principal, Long id, AttendanceArriveUpdateContentRequest attendanceArriveUpdateContentRequest) throws IOException {
        AttendanceArriveKids attendanceArriveKids = attendanceArriveKidsRepository.findById(id).orElseThrow(() -> new NotFoundException("not found arrive kids by id"));
        if (attendanceArriveUpdateContentRequest.getMultipartFile() != null) {
            String urlLocalOld = attendanceArriveKids.getArriveUrlPictureLocal();
            HandleFileUtils.deletePictureInFolder(urlLocalOld);
            String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.DIEM_DANH);
            String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, attendanceArriveUpdateContentRequest.getMultipartFile());
            HandleFileUtils.createFilePictureToDirectory(urlFolder, attendanceArriveUpdateContentRequest.getMultipartFile(), fileName, UploadDownloadConstant.WIDTH_OTHER);

            String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.DIEM_DANH) + fileName;
            String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.DIEM_DANH) + fileName;
            attendanceArriveKids.setArriveUrlPicture(urlWeb);
            attendanceArriveKids.setArriveUrlPictureLocal(urlLocal);
        }
        attendanceArriveKids.setArriveContent(attendanceArriveUpdateContentRequest.getArriveContent());
        attendanceArriveKids.setIdModified(principal.getId());
        attendanceArriveKids.setLastModifieBy(principal.getFullName());
        attendanceArriveKids.setLastModifieDate(LocalDateTime.now());
        attendanceArriveKidsRepository.save(attendanceArriveKids);
        return true;
    }

    @Transactional
    @Override
    public boolean saveAttendanceArriveManyKidsDate(Long idSchool, UserPrincipal principal, List<AttendanceArriveKidsDateRequest> attendanceArriveKidsDateRequestList) throws FirebaseMessagingException {
        /**
         * thực hiện vòng lặp for để lưu các điểm danh đến cho các học sinh đã chọn
         */
        for (AttendanceArriveKidsDateRequest attendanceArriveKidsDateRequest : attendanceArriveKidsDateRequestList) {
            Optional<AttendanceKids> attendanceKidsOptional = attendanceKidsRepository.findByIdAttendance(idSchool, attendanceArriveKidsDateRequest.getId());
            if (attendanceKidsOptional.isEmpty()) {
                return false;
            }
            AttendanceKids attendanceKids = attendanceKidsOptional.get();
            modelMapper.map(attendanceArriveKidsDateRequest, attendanceKids);
            //set auditing cho điểm danh đến
            boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_ARRIVE, principal, attendanceKids);
            attendanceKids.setAttendanceArrive(this.getAttendanceArriveStastus(attendanceArriveKidsDateRequest.getAttendanceArriveKids()));
            this.resetAttendanceLeaveKids(attendanceKids);
            AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKids);
            //tự động điểm danh ăn theo config
            this.saveAttendanceEatAuto(attendanceKidsSaved);
            if (checkSendFirebase) {
                long idWebSystem = 0;
                if (AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
                    idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.SCHOOL);
                } else if (AppTypeConstant.TEACHER.equals(principal.getAppType())) {
                    idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.TEACHER);
                }
                if (idWebSystem != 0) {
                    //send firebase
                    firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
                }
            }
        }
        return true;
    }

    @Override
    public List<AttendanceArriveKidsDateResponse> findAttendanceArriveKidsDetailOfMonth(Long idSchool, Long idKid, Integer month, Integer year) {
        if (idKid == null || month == null || year == null) {
            return null;
        }
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsDetailOfMonth(idSchool, idKid, dateStart, dateEnd);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return null;
        }
        List<AttendanceArriveKidsDateResponse> attendanceArriveKidsDateResponseList = listMapper.mapList(attendanceKidsList, AttendanceArriveKidsDateResponse.class);
        return attendanceArriveKidsDateResponseList;
    }

    @Override
    public List<AttendanceLeaveKidsDateResponse> searchAttendanceLeaveKidsDate(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.searchAllAttendanceKidsDate(idSchool, AttendanceConstant.ATTENDANCE_LEAVE, attendanceKidsSearchRequest);
        List<AttendanceLeaveKidsDateResponse> attendanceLeaveKidsDateResponseList = listMapper.mapList(attendanceKidsList, AttendanceLeaveKidsDateResponse.class);
        return attendanceLeaveKidsDateResponseList;
    }

    @Transactional
    @Override
    public AttendanceLeaveKidsDateResponse saveAttendanceLeaveOneKidsDate(Long idSchool, UserPrincipal principal, AttendanceLeaveKidsDateRequest attendanceLeaveKidsDateRequest) throws FirebaseMessagingException {
        AttendanceKids attendanceKids = attendanceKidsRepository.findByIdAttendance(idSchool, attendanceLeaveKidsDateRequest.getId()).orElseThrow(() -> new NotFoundException("not found attendance by id"));
        this.checkAttendanceLeave(attendanceKids, attendanceLeaveKidsDateRequest);
        modelMapper.map(attendanceLeaveKidsDateRequest, attendanceKids);
        //set auditing cho điểm danh về
        boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_LEAVE, principal, attendanceKids);
        attendanceKids.setAttendanceLeave(this.getAttendanceLeaveStastus(attendanceLeaveKidsDateRequest.getAttendanceLeaveKids()));
        AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKids);
        AttendanceLeaveKidsDateResponse attendanceLeaveKidsDateResponse = modelMapper.map(attendanceKidsSaved, AttendanceLeaveKidsDateResponse.class);
        if (checkSendFirebase) {
            long idWebSystem = 0;
            if (AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
                idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.SCHOOL);
            } else if (AppTypeConstant.TEACHER.equals(principal.getAppType())) {
                idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.TEACHER);
            }
            if (idWebSystem != 0) {
                //send firebase
                firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
            }
        }
        return attendanceLeaveKidsDateResponse;
    }


    @Override
    public boolean updateContentLeave(Long idSchool, UserPrincipal principal, Long id, AttendanceLeaveUpdateContentRequest attendanceLeaveUpdateContentRequest) throws IOException {
        AttendanceLeaveKids attendanceLeaveKids = attendanceLeaveKidsRepository.findById(id).orElseThrow(() -> new NotFoundException("not found leave kids by id"));
        if (attendanceLeaveUpdateContentRequest.getMultipartFile() != null) {
            String urlLocalOld = attendanceLeaveKids.getLeaveUrlPictureLocal();
            HandleFileUtils.deletePictureInFolder(urlLocalOld);

            String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.DIEM_DANH);
            String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, attendanceLeaveUpdateContentRequest.getMultipartFile());
            HandleFileUtils.createFilePictureToDirectory(urlFolder, attendanceLeaveUpdateContentRequest.getMultipartFile(), fileName, UploadDownloadConstant.WIDTH_OTHER);

            String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.DIEM_DANH) + fileName;
            String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.DIEM_DANH) + fileName;
            attendanceLeaveKids.setLeaveUrlPicture(urlWeb);
            attendanceLeaveKids.setLeaveUrlPictureLocal(urlLocal);
        }
        attendanceLeaveKids.setLeaveContent(attendanceLeaveUpdateContentRequest.getLeaveContent());
        attendanceLeaveKids.setIdModified(principal.getId());
        attendanceLeaveKids.setLastModifieBy(principal.getFullName());
        attendanceLeaveKids.setLastModifieDate(LocalDateTime.now());
        attendanceLeaveKidsRepository.save(attendanceLeaveKids);
        return true;
    }

    @Transactional
    @Override
    public boolean saveAttendanceLeaveManyKidsDate(Long idSchool, UserPrincipal principal, List<AttendanceLeaveKidsDateRequest> attendanceLeaveKidsDateRequestList) throws FirebaseMessagingException {
        for (AttendanceLeaveKidsDateRequest x : attendanceLeaveKidsDateRequestList) {
            AttendanceKids attendanceKids = attendanceKidsRepository.findByIdAttendance(idSchool, x.getId()).orElseThrow(() -> new NotFoundException("not found attendance by id"));
            this.checkAttendanceLeave(attendanceKids, x);
            modelMapper.map(x, attendanceKids);
            //set auditing cho điểm danh về
            boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_LEAVE, principal, attendanceKids);
            attendanceKids.setAttendanceLeave(this.getAttendanceLeaveStastus(x.getAttendanceLeaveKids()));
            AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKids);
            if (checkSendFirebase) {
                long idWebSystem = 0;
                if (AppTypeConstant.SCHOOL.equals(principal.getAppType())) {
                    idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.SCHOOL);
                } else if (AppTypeConstant.TEACHER.equals(principal.getAppType())) {
                    idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.TEACHER);
                }
                if (idWebSystem != 0) {
                    //send firebase
                    firebaseFunctionService.sendParentByPlusNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE);
                }
            }
        }
        return true;
    }

    @Override
    public List<AttendanceLeaveKidsDateResponse> findAttendanceLeaveKidsDetailOfMonth(Long idSchool, Long idKid, Integer month, Integer year) {
        if (idKid == null || month == null || year == null) {
            return null;
        }
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsDetailOfMonth(idSchool, idKid, dateStart, dateEnd);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return null;
        }
        return listMapper.mapList(attendanceKidsList, AttendanceLeaveKidsDateResponse.class);
    }

    @Override
    public ListAttendanceEatKidsDateResponse searchAttendanceEatKidsDate(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        ListAttendanceEatKidsDateResponse model = new ListAttendanceEatKidsDateResponse();
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.searchAllAttendanceKidsDate(idSchool, AttendanceConstant.ATTENDANCE_EAT, attendanceKidsSearchRequest);
        List<AttendanceEatKidsDateResponse> attendanceEatKidsDateResponseList = listMapper.mapList(attendanceKidsList, AttendanceEatKidsDateResponse.class);
        AttendanceConfigResponse attendanceConfigResponse = this.getAttendanceConfig(idSchool, attendanceKidsList, attendanceKidsSearchRequest.getDate());
        model.setAttendanceEatList(attendanceEatKidsDateResponseList);
        model.setAttendanceConfigResponse(attendanceConfigResponse);
        return model;
    }

    @Transactional
    @Override
    public AttendanceEatKidsDateResponse saveAttendanceEatOneKidsDate(Long idSchool, UserPrincipal principal, AttendanceEatKidsDateRequest attendanceEatKidsDateRequest) {
        AttendanceKids attendanceKids = attendanceKidsRepository.findByIdAttendance(idSchool, attendanceEatKidsDateRequest.getId()).orElseThrow(() -> new NotFoundException("not found attendanceKids by id"));
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.getAttendanceConfig(), attendanceKids.getAttendanceDate());
        if (!attendanceConfigResponse.isMorningEat()) {
            attendanceEatKidsDateRequest.getAttendanceEatKids().setBreakfast(AppConstant.APP_FALSE);
        }
        if (!attendanceConfigResponse.isSecondMorningEat()) {
            attendanceEatKidsDateRequest.getAttendanceEatKids().setSecondBreakfast(AppConstant.APP_FALSE);
        }
        if (!attendanceConfigResponse.isLunchEat()) {
            attendanceEatKidsDateRequest.getAttendanceEatKids().setLunch(AppConstant.APP_FALSE);
        }
        if (!attendanceConfigResponse.isAfternoonEat()) {
            attendanceEatKidsDateRequest.getAttendanceEatKids().setAfternoon(AppConstant.APP_FALSE);
        }
        if (!attendanceConfigResponse.isSecondAfternoonEat()) {
            attendanceEatKidsDateRequest.getAttendanceEatKids().setSecondAfternoon(AppConstant.APP_FALSE);
        }
        if (!attendanceConfigResponse.isEveningEat()) {
            attendanceEatKidsDateRequest.getAttendanceEatKids().setDinner(AppConstant.APP_FALSE);
        }
        modelMapper.map(attendanceEatKidsDateRequest, attendanceKids);
        //set auditing cho điểm danh ăn
        AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_EAT, principal, attendanceKids);
        attendanceKids.setAttendanceEat(this.getAttendanceEatStastus(attendanceEatKidsDateRequest.getAttendanceEatKids()));
        AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKids);
        AttendanceEatKidsDateResponse attendanceEatKidsDateResponse = modelMapper.map(attendanceKidsSaved, AttendanceEatKidsDateResponse.class);
        return attendanceEatKidsDateResponse;
    }

    @Transactional
    @Override
    public boolean saveAttendanceEatManyKidsDate(Long idSchool, UserPrincipal principal, List<AttendanceEatKidsDateRequest> attendanceEatKidsDateRequestList) {
        attendanceEatKidsDateRequestList.forEach(x -> {
            AttendanceKids attendanceKids = attendanceKidsRepository.findByIdAttendance(idSchool, x.getId()).orElseThrow(() -> new NotFoundException("not found attendanceKids by id"));
            modelMapper.map(x, attendanceKids);
            //set auditing cho điểm danh ăn
            AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_EAT, principal, attendanceKids);
            attendanceKids.setAttendanceEat(this.getAttendanceEatStastus(x.getAttendanceEatKids()));
            attendanceKidsRepository.save(attendanceKids);
        });
        return true;
    }

    @Override
    public List<AttendanceEatKidsDateResponse> findAttendanceEatKidsDetailOfMonth(Long idSchool, Long idKid, Integer month, Integer year) {
        if (idKid == null || month == null || year == null) {
            return null;
        }
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsDetailOfMonth(idSchool, idKid, dateStart, dateEnd);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return null;
        }
        return listMapper.mapList(attendanceKidsList, AttendanceEatKidsDateResponse.class);
    }


    /**
     * chuyển đổi học sinh sang view excel
     *
     * @param
     */

    @Override
    public List<AttendanceKidsModel> getFileAttendanceKids(ListAttendanceKidsDetailDateResponse data) {

        List<AttendanceKidsModel> attendanceKidsModels = new ArrayList<>();
        long i = 1;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        List<AttendanceKidsDetailDateResponse> dataAttendanceKidList = data.getAttendanceDetailDateList();

        for (AttendanceKidsDetailDateResponse model : dataAttendanceKidList) {
            AttendanceKidsModel attendanceKidsModel = new AttendanceKidsModel();
            attendanceKidsModel.setId(i++);

            if (model.getKids().getFullName() != null) {
                attendanceKidsModel.setKidName(model.getKids().getFullName());
            } else {
                attendanceKidsModel.setKidName("");
            }
            if (model.getAttendanceArriveKids().isEvening() || model.getAttendanceArriveKids().isEveningYes() || model.getAttendanceArriveKids().isEveningNo()) {
                if (model.getAttendanceArriveKids().isMorning() && model.getAttendanceArriveKids().isAfternoon() && model.getAttendanceArriveKids().isEvening()) {
                    attendanceKidsModel.setAbsentStatus("x");
                } else {
                    attendanceKidsModel.setAbsentStatus("");
                }
                if (model.getAttendanceArriveKids().isMorningYes() && model.getAttendanceArriveKids().isAfternoonYes() && model.getAttendanceArriveKids().isEveningYes()) {
                    attendanceKidsModel.setAbsentLetterYes("x");
                } else {
                    attendanceKidsModel.setAbsentLetterYes("");
                }
                if (model.getAttendanceArriveKids().isMorningNo() && model.getAttendanceArriveKids().isAfternoonNo() && model.getAttendanceArriveKids().isEveningNo()) {
                    attendanceKidsModel.setAbsentLetterNo("x");
                } else {
                    attendanceKidsModel.setAbsentLetterNo("");
                }
            } else {
                if (model.getAttendanceArriveKids().isMorning() && model.getAttendanceArriveKids().isAfternoon()) {
                    attendanceKidsModel.setAbsentStatus("x");
                } else {
                    attendanceKidsModel.setAbsentStatus("");
                }
                if (model.getAttendanceArriveKids().isMorningYes() && model.getAttendanceArriveKids().isAfternoonYes()) {
                    attendanceKidsModel.setAbsentLetterYes("x");
                } else {
                    attendanceKidsModel.setAbsentLetterYes("");
                }
                if (model.getAttendanceArriveKids().isMorningNo() && model.getAttendanceArriveKids().isAfternoonNo()) {
                    attendanceKidsModel.setAbsentLetterNo("x");
                } else {
                    attendanceKidsModel.setAbsentLetterNo("");
                }
            }
            if (model.getAttendanceArriveKids().isMorning()) {
                attendanceKidsModel.setMorning("x");
            } else {
                attendanceKidsModel.setMorning("");
            }
            if (model.getAttendanceArriveKids().isMorningNo()) {
                attendanceKidsModel.setMorningNo("x");
            } else {
                attendanceKidsModel.setMorningNo("");
            }
            if (model.getAttendanceArriveKids().isMorningYes()) {
                attendanceKidsModel.setMorningYes("x");
            } else {
                attendanceKidsModel.setMorningYes("");
            }
            if (model.getAttendanceArriveKids().isAfternoon()) {
                attendanceKidsModel.setAfternoon("x");
            } else {
                attendanceKidsModel.setAfternoon("");
            }

            if (model.getAttendanceArriveKids().isAfternoonNo()) {
                attendanceKidsModel.setAfternoonNo("x");
            } else {
                attendanceKidsModel.setAfternoonNo("");
            }
            if (model.getAttendanceArriveKids().isAfternoonYes()) {
                attendanceKidsModel.setAfternoonYes("x");
            } else {
                attendanceKidsModel.setAfternoonYes("");
            }
            if (model.getAttendanceArriveKids().isEvening()) {
                attendanceKidsModel.setEvening("x");
            } else {
                attendanceKidsModel.setEvening("");
            }
            if (model.getAttendanceArriveKids().isEveningNo()) {
                attendanceKidsModel.setEveningNo("x");
            } else {
                attendanceKidsModel.setEveningNo("");
            }
            if (model.getAttendanceArriveKids().isEveningYes()) {
                attendanceKidsModel.setEveningYes("x");
            } else {
                attendanceKidsModel.setEveningYes("");
            }
            if (model.getAttendanceEatKids().isBreakfast()) {
                attendanceKidsModel.setEatBreakfast("x");
            } else {
                attendanceKidsModel.setEatBreakfast("");
            }
            if (model.getAttendanceEatKids().isSecondBreakfast()) {
                attendanceKidsModel.setEatSecondBreakfast("x");
            } else {
                attendanceKidsModel.setEatSecondBreakfast("");
            }
            if (model.getAttendanceEatKids().isLunch()) {
                attendanceKidsModel.setEatLunch("x");
            } else {
                attendanceKidsModel.setEatLunch("");
            }
            if (model.getAttendanceEatKids().isAfternoon()) {
                attendanceKidsModel.setEatAfternoon("x");
            } else {
                attendanceKidsModel.setEatAfternoon("");
            }
            if (model.getAttendanceEatKids().isSecondAfternoon()) {
                attendanceKidsModel.setEatSecondAfternoon("x");
            } else {
                attendanceKidsModel.setEatSecondAfternoon("");
            }
            if (model.getAttendanceEatKids().isDinner()) {
                attendanceKidsModel.setEatDinner("x");
            } else {
                attendanceKidsModel.setEatDinner("");
            }
            if (model.getAttendanceArriveKids().getTimeArriveKid() != null) {
                String timeArriveKid = model.getAttendanceArriveKids().getTimeArriveKid().format(dtf);
                attendanceKidsModel.setTimeArriveKid(timeArriveKid);
            } else {
                attendanceKidsModel.setTimeArriveKid("");
            }
            if (model.getAttendanceLeaveKids().getTimeLeaveKid() != null) {
                String timeLeaveKid = model.getAttendanceLeaveKids().getTimeLeaveKid().format(dtf);
                attendanceKidsModel.setTimeLeaveKid(timeLeaveKid);
            } else {
                attendanceKidsModel.setTimeLeaveKid("");
            }
            if (model.getAttendanceLeaveKids().getMinutePickupLate() != 0) {
                String mutePickupLate = String.valueOf(model.getAttendanceLeaveKids().getMinutePickupLate());
                attendanceKidsModel.setMinutePickupLate(mutePickupLate);
            } else {
                attendanceKidsModel.setMinutePickupLate("");
            }
            attendanceKidsModels.add(attendanceKidsModel);

        }
        return attendanceKidsModels;
    }

    @Override
    public List<ExcelNewResponse> getFileAttendanceKidsNew(ListAttendanceKidsDetailDateResponse dateResponse, Long idSchool, Long idClass, LocalDate date) {
        List<ExcelNewResponse> responseList = new ArrayList<>();
        ExcelNewResponse response = new ExcelNewResponse();
        List<ExcelDataNew> bodyList = new ArrayList<>();

        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
        String schoolName = schoolResponse != null ? schoolResponse.getSchoolName() : "";
        MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
        String className = classDTO != null ? classDTO.getClassName() : "";
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateToStrSheet = df1.format(date);

        List<String> headerStringList = Arrays.asList("BẢNG KÊ ĐIỂM DANH NGÀY", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(className), AppConstant.EXCEL_DATE.concat(ConvertData.convertLocalDateToString(date)));
        List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
        ExcelDataNew headerMulti = this.setHeaderMulti();
        ExcelDataNew headerMulti1 = this.setHeaderMulti1();
        headerList.add(headerMulti);
        headerList.add(headerMulti1);
        response.setSheetName(dateToStrSheet);
        response.setHeaderList(headerList);
        List<AttendanceKidsModel> attendanceKidsModels = this.setAttendanceKidsModel(dateResponse);
        for (AttendanceKidsModel x : attendanceKidsModels) {
            List<String> bodyStringList = Arrays.asList(String.valueOf(x.getId()), x.getKidName(), x.getAbsentLetterYes(), x.getAbsentLetterNo(), x.getAbsentStatus(), x.getMorningYes(), x.getMorningNo(), x.getMorning(), x.getAfternoonYes(), x.getAfternoonNo(), x.getAfternoon(), x.getEveningYes(), x.getEveningNo(), x.getEvening(), x.getEatBreakfast(), x.getEatSecondBreakfast(), x.getEatLunch(), x.getEatAfternoon(), x.getEatSecondAfternoon(), x.getEatDinner(), x.getTimeArriveKid(), x.getTimeLeaveKid(), x.getMinutePickupLate());
            ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
            bodyList.add(modelData);
        }
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public List<ListIdKidDTO> listIdAttendanceKidsDetailOfMonth(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest) {
        if (attendanceKidsSearchRequest.getDate() == null) {
            return null;
        }
        int month = attendanceKidsSearchRequest.getDate().getMonthValue();
        int year = attendanceKidsSearchRequest.getDate().getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        int monNow = LocalDate.now().getMonthValue();
        int yearNow = LocalDate.now().getYear();
        if (month == monNow && year == yearNow) {
            dateEnd = LocalDate.now().plusDays(1);
        }
        return attendanceKidsRepository.totalAttendanceKidsDetailOfMonth(idSchool, attendanceKidsSearchRequest.getIdClass(), dateStart, dateEnd);

    }

    @Override
    public List<AttendanceKidsDetailDateResponse> findAttendanceKidsClassOfMonth(Long idSchool, AttendanceKidsSearchRequest attendanceKidsSearchRequest) {

        int month = attendanceKidsSearchRequest.getDate().getMonthValue();
        int year = attendanceKidsSearchRequest.getDate().getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1);
        int monNow = LocalDate.now().getMonthValue();
        int yearNow = LocalDate.now().getYear();
        if (month == monNow && year == yearNow) {
            dateEnd = LocalDate.now().plusDays(1);
        }


        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsClassOfMonth(idSchool, attendanceKidsSearchRequest.getIdClass(), dateStart, dateEnd);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return null;
        }
        return listMapper.mapList(attendanceKidsList, AttendanceKidsDetailDateResponse.class);
    }

    @Override
    public Map<Long, List<AttendanceKidsModel>> detachedListAttendanceKidsClassOfMonth(List<AttendanceKidsDetailDateResponse> listAttendanceKidsDetailDateResponse, List<ListIdKidDTO> kidDTOList) {

        if (listAttendanceKidsDetailDateResponse == null || kidDTOList == null) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Map<Long, List<AttendanceKidsModel>> mapAttendance = new HashMap<>();

        for (ListIdKidDTO listIdKidDTO : kidDTOList) {
            List<AttendanceKidsDetailDateResponse> dateResponseList = new ArrayList<>();
            for (AttendanceKidsDetailDateResponse attendanceKidsDateResponse : listAttendanceKidsDetailDateResponse) {
                if (attendanceKidsDateResponse.getKids().getId().equals(listIdKidDTO.getId())) {
                    dateResponseList.add(attendanceKidsDateResponse);
                }
            }
            List<AttendanceKidsModel> attendanceKidsModels = new ArrayList<>();
            long i = 1;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");


            for (AttendanceKidsDetailDateResponse model : dateResponseList) {
                AttendanceKidsModel attendanceKidsModel = new AttendanceKidsModel();
                attendanceKidsModel.setId(i++);

                if (model.getKids() == null) {
                    continue;
                }
                String date = df.format(model.getAttendanceDate());
                attendanceKidsModel.setAttendanceDate(date);
                attendanceKidsModel.setKidName(model.getKids().getFullName());
                if (model.getAttendanceArriveKids().isEvening() || model.getAttendanceArriveKids().isEveningYes() || model.getAttendanceArriveKids().isEveningNo()) {
                    if (model.getAttendanceArriveKids().isMorning() && model.getAttendanceArriveKids().isAfternoon() && model.getAttendanceArriveKids().isEvening()) {
                        attendanceKidsModel.setAbsentStatus("x");
                    } else {
                        attendanceKidsModel.setAbsentStatus("");
                    }
                    if (model.getAttendanceArriveKids().isMorningYes() && model.getAttendanceArriveKids().isAfternoonYes() && model.getAttendanceArriveKids().isEveningYes()) {
                        attendanceKidsModel.setAbsentLetterYes("x");
                    } else {
                        attendanceKidsModel.setAbsentLetterYes("");
                    }
                    if (model.getAttendanceArriveKids().isMorningNo() && model.getAttendanceArriveKids().isAfternoonNo() && model.getAttendanceArriveKids().isEveningNo()) {
                        attendanceKidsModel.setAbsentLetterNo("x");
                    } else {
                        attendanceKidsModel.setAbsentLetterNo("");
                    }
                } else {
                    if (model.getAttendanceArriveKids().isMorning() && model.getAttendanceArriveKids().isAfternoon()) {
                        attendanceKidsModel.setAbsentStatus("x");
                    } else {
                        attendanceKidsModel.setAbsentStatus("");
                    }
                    if (model.getAttendanceArriveKids().isMorningYes() && model.getAttendanceArriveKids().isAfternoonYes()) {
                        attendanceKidsModel.setAbsentLetterYes("x");
                    } else {
                        attendanceKidsModel.setAbsentLetterYes("");
                    }
                    if (model.getAttendanceArriveKids().isMorningNo() && model.getAttendanceArriveKids().isAfternoonNo()) {
                        attendanceKidsModel.setAbsentLetterNo("x");
                    } else {
                        attendanceKidsModel.setAbsentLetterNo("");
                    }
                }
                if (model.getAttendanceArriveKids().isMorning()) {
                    attendanceKidsModel.setMorning("x");
                } else {
                    attendanceKidsModel.setMorning("");
                }
                if (model.getAttendanceArriveKids().isMorningNo()) {
                    attendanceKidsModel.setMorningNo("x");
                } else {
                    attendanceKidsModel.setMorningNo("");
                }
                if (model.getAttendanceArriveKids().isMorningYes()) {
                    attendanceKidsModel.setMorningYes("x");
                } else {
                    attendanceKidsModel.setMorningYes("");
                }
                if (model.getAttendanceArriveKids().isAfternoon()) {
                    attendanceKidsModel.setAfternoon("x");
                } else {
                    attendanceKidsModel.setAfternoon("");
                }

                if (model.getAttendanceArriveKids().isAfternoonNo()) {
                    attendanceKidsModel.setAfternoonNo("x");
                } else {
                    attendanceKidsModel.setAfternoonNo("");
                }
                if (model.getAttendanceArriveKids().isAfternoonYes()) {
                    attendanceKidsModel.setAfternoonYes("x");
                } else {
                    attendanceKidsModel.setAfternoonYes("");
                }
                if (model.getAttendanceArriveKids().isEvening()) {
                    attendanceKidsModel.setEvening("x");
                } else {
                    attendanceKidsModel.setEvening("");
                }
                if (model.getAttendanceArriveKids().isEveningNo()) {
                    attendanceKidsModel.setEveningNo("x");
                } else {
                    attendanceKidsModel.setEveningNo("");
                }
                if (model.getAttendanceArriveKids().isEveningYes()) {
                    attendanceKidsModel.setEveningYes("x");
                } else {
                    attendanceKidsModel.setEveningYes("");
                }
                if (model.getAttendanceEatKids().isBreakfast()) {
                    attendanceKidsModel.setEatBreakfast("x");
                } else {
                    attendanceKidsModel.setEatBreakfast("");
                }
                if (model.getAttendanceEatKids().isSecondBreakfast()) {
                    attendanceKidsModel.setEatSecondBreakfast("x");
                } else {
                    attendanceKidsModel.setEatSecondBreakfast("");
                }
                if (model.getAttendanceEatKids().isLunch()) {
                    attendanceKidsModel.setEatLunch("x");
                } else {
                    attendanceKidsModel.setEatLunch("");
                }
                if (model.getAttendanceEatKids().isAfternoon()) {
                    attendanceKidsModel.setEatAfternoon("x");
                } else {
                    attendanceKidsModel.setEatAfternoon("");
                }
                if (model.getAttendanceEatKids().isSecondAfternoon()) {
                    attendanceKidsModel.setEatSecondAfternoon("x");
                } else {
                    attendanceKidsModel.setEatSecondAfternoon("");
                }
                if (model.getAttendanceEatKids().isDinner()) {
                    attendanceKidsModel.setEatDinner("x");
                } else {
                    attendanceKidsModel.setEatDinner("");
                }
                if (model.getAttendanceArriveKids().getTimeArriveKid() != null) {
                    String timeArriveKid = model.getAttendanceArriveKids().getTimeArriveKid().format(dtf);
                    attendanceKidsModel.setTimeArriveKid(timeArriveKid);
                } else {
                    attendanceKidsModel.setTimeArriveKid("");
                }
                if (model.getAttendanceLeaveKids().getTimeLeaveKid() != null) {
                    String timeLeaveKid = model.getAttendanceLeaveKids().getTimeLeaveKid().format(dtf);
                    attendanceKidsModel.setTimeLeaveKid(timeLeaveKid);
                } else {
                    attendanceKidsModel.setTimeLeaveKid("");
                }
                if (model.getAttendanceLeaveKids().getMinutePickupLate() != 0) {
                    String mutePickupLate = String.valueOf(model.getAttendanceLeaveKids().getMinutePickupLate());
                    attendanceKidsModel.setMinutePickupLate(mutePickupLate);
                } else {
                    attendanceKidsModel.setMinutePickupLate("");
                }
                attendanceKidsModels.add(attendanceKidsModel);

            }
            mapAttendance.put(listIdKidDTO.getId(), attendanceKidsModels);

        }

        return mapAttendance;
    }

    @Override
    public List<ExcelNewResponse> detachedListAttendanceKidsClassOfMonthNew(List<AttendanceKidsDetailDateResponse> listAttendanceKidsDetailDateResponse, List<ListIdKidDTO> kidDTOList, Long idSchool, Long idClass, LocalDate date) {
        if (listAttendanceKidsDetailDateResponse == null || kidDTOList == null) {
            return null;
        }
        List<ExcelNewResponse> responseList = new ArrayList<>();
        SchoolResponse schoolResponse = schoolService.findByIdSchool(idSchool).stream().findFirst().orElse(null);
        String schoolName = schoolResponse != null ? schoolResponse.getSchoolName() : "";
        MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, idClass).stream().findFirst().orElse(null);
        String className = classDTO != null ? classDTO.getClassName() : "";

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int month = date.getMonthValue();
        int year = date.getYear();
        LocalDate dateStart = LocalDate.of(year, month, 1);
        LocalDate dateEnd = dateStart.plusMonths(1).minusDays(1);
        String dateToStr = df.format(dateStart);
        String dateToStrSheet = df.format(dateEnd);

        for (ListIdKidDTO listIdKidDTO : kidDTOList) {
            ExcelNewResponse response = new ExcelNewResponse();
            List<ExcelDataNew> bodyList = new ArrayList<>();
            Kids kids = kidsRepository.findById(listIdKidDTO.getId()).orElseThrow();
            String kidsName = kids.getFullName();
            List<String> headerStringList = Arrays.asList("BẢNG KÊ ĐIỂM DANH THÁNG", AppConstant.EXCEL_SCHOOL.concat(schoolName), AppConstant.EXCEL_CLASS.concat(className), AppConstant.EXCEL_DATE.concat(dateToStr.concat(" - ").concat(dateToStrSheet)));
            List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
            ExcelDataNew headerMulti = this.setHeaderMulti();
            ExcelDataNew headerMulti2 = this.setHeaderMulti2();
            headerList.add(headerMulti);
            headerList.add(headerMulti2);
            response.setSheetName(kidsName.concat(AppConstant.SPACE_EXPORT_ID.concat(kids.getId().toString())));
            response.setHeaderList(headerList);
            List<AttendanceKidsModel> attendanceKidsModelList = this.setAttendanceDetail(listAttendanceKidsDetailDateResponse, listIdKidDTO);
            for (AttendanceKidsModel x : attendanceKidsModelList) {
                List<String> bodyStringList = Arrays.asList(String.valueOf(x.getId()), x.getAttendanceDate(), x.getAbsentLetterYes(), x.getAbsentLetterNo(), x.getAbsentStatus(), x.getMorningYes(), x.getMorningNo(), x.getMorning(), x.getAfternoonYes(), x.getAfternoonNo(), x.getAfternoon(), x.getEveningYes(), x.getEveningNo(), x.getEvening(), x.getEatBreakfast(), x.getEatSecondBreakfast(), x.getEatLunch(), x.getEatAfternoon(), x.getEatSecondAfternoon(), x.getEatDinner(), x.getTimeArriveKid(), x.getTimeLeaveKid(), x.getMinutePickupLate());
                ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
                bodyList.add(modelData);
            }
            response.setBodyList(bodyList);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public List<AttendanceKidsDetailDateResponse> findAttendanceKidsClassCustom(Long idSchool, AttendanceKidsSearchCustomRequest request) {
        LocalDate dateStart = request.getDateStartEnd().get(0);
        LocalDate dateEnd = request.getDateStartEnd().get(1);
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidsClassOfMonth(idSchool, request.getIdClass(), dateStart, dateEnd);
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            return null;
        }
        return listMapper.mapList(attendanceKidsList, AttendanceKidsDetailDateResponse.class);
    }

    @Override
    public List<ListIdKidDTO> listIdAttendanceKidsDetailCustom(Long idSchool, AttendanceKidsSearchCustomRequest request) {
        LocalDate dateStart = request.getDateStartEnd().get(0);
        LocalDate dateEnd = request.getDateStartEnd().get(1);
        return attendanceKidsRepository.totalAttendanceKidsDetailOfMonth(idSchool, request.getIdClass(), dateStart, dateEnd);
    }

    @Override
    public void saveAttendanceEatAuto(AttendanceKids attendanceKids) {
        SchoolConfigResponse schoolConfigResponse = PrincipalUtils.getUserPrincipal().getSchoolConfig();
        boolean checkArrive = AttendanceKidsUtil.checkArrive(attendanceKids.getAttendanceArriveKids());
        AttendanceEatKids eatKids = attendanceKids.getAttendanceEatKids();
        if (checkArrive) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.getAttendanceConfig(), attendanceKids.getAttendanceDate());
            if (attendanceConfigResponse.isMorningEat()) {
                eatKids.setBreakfast(schoolConfigResponse.isBreakfastAuto());
            }
            if (attendanceConfigResponse.isSecondMorningEat()) {
                eatKids.setSecondBreakfast(schoolConfigResponse.isSecondBreakfastAuto());
            }
            if (attendanceConfigResponse.isLunchEat()) {
                eatKids.setLunch(schoolConfigResponse.isLunchAuto());
            }
            if (attendanceConfigResponse.isAfternoonEat()) {
                eatKids.setAfternoon(schoolConfigResponse.isAfternoonAuto());
            }
            if (attendanceConfigResponse.isSecondAfternoonEat()) {
                eatKids.setSecondAfternoon(schoolConfigResponse.isSecondAfternoonAuto());
            }
            if (attendanceConfigResponse.isEveningEat()) {
                eatKids.setDinner(schoolConfigResponse.isDinnerAuto());
            }
        } else {
            eatKids.setBreakfast(false);
            eatKids.setSecondBreakfast(false);
            eatKids.setLunch(false);
            eatKids.setAfternoon(false);
            eatKids.setSecondAfternoon(false);
            eatKids.setDinner(false);
        }
        attendanceKidsRepository.save(attendanceKids);
    }

    private ExcelDataNew setHeaderMulti() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("ĐIỂM DANH");
        headerMulti.setPro2("");
        headerMulti.setPro3("TRẠNG THÁI ĐI HỌC");
        headerMulti.setPro4("");
        headerMulti.setPro5("");
        headerMulti.setPro6("BUỔI HỌC");
        headerMulti.setPro7("");
        headerMulti.setPro8("");
        headerMulti.setPro9("");
        headerMulti.setPro10("");
        headerMulti.setPro11("");
        headerMulti.setPro12("");
        headerMulti.setPro13("");
        headerMulti.setPro14("");
        headerMulti.setPro15("BỮA ĂN");
        headerMulti.setPro16("");
        headerMulti.setPro17("");
        headerMulti.setPro18("");
        headerMulti.setPro19("");
        headerMulti.setPro20("");
        headerMulti.setPro21("GIỜ ĐƯA ĐÓN");
        headerMulti.setPro22("");
        headerMulti.setPro23("ĐÓN MUỘN");
        return headerMulti;
    }

    private ExcelDataNew setHeaderMulti1() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("STT");
        headerMulti.setPro2("Họ và tên");
        headerMulti.setPro3("Nghỉ có phép");
        headerMulti.setPro4("Nghỉ không phép");
        headerMulti.setPro5("Đi học");
        headerMulti.setPro6("Sáng");
        headerMulti.setPro7("");
        headerMulti.setPro8("");
        headerMulti.setPro9("Chiều");
        headerMulti.setPro10("");
        headerMulti.setPro11("");
        headerMulti.setPro12("Tối");
        headerMulti.setPro13("");
        headerMulti.setPro14("");
        headerMulti.setPro15("Sáng");
        headerMulti.setPro16("Phụ sáng");
        headerMulti.setPro17("Trưa");
        headerMulti.setPro18("Chiều");
        headerMulti.setPro19("Phụ chiều");
        headerMulti.setPro20("Tối");
        headerMulti.setPro21("Đến");
        headerMulti.setPro22("Về");
        headerMulti.setPro23("Phút");
        return headerMulti;
    }

    private ExcelDataNew setHeaderMulti2() {
        ExcelDataNew headerMulti = new ExcelDataNew();
        headerMulti.setPro1("STT");
        headerMulti.setPro2("Thời gian");
        headerMulti.setPro3("Nghỉ có phép");
        headerMulti.setPro4("Nghỉ không phép");
        headerMulti.setPro5("Đi học");
        headerMulti.setPro6("Sáng");
        headerMulti.setPro7("");
        headerMulti.setPro8("");
        headerMulti.setPro9("Chiều");
        headerMulti.setPro10("");
        headerMulti.setPro11("");
        headerMulti.setPro12("Tối");
        headerMulti.setPro13("");
        headerMulti.setPro14("");
        headerMulti.setPro15("Sáng");
        headerMulti.setPro16("Phụ sáng");
        headerMulti.setPro17("Trưa");
        headerMulti.setPro18("Chiều");
        headerMulti.setPro19("Phụ chiều");
        headerMulti.setPro20("Tối");
        headerMulti.setPro21("Đến");
        headerMulti.setPro22("Về");
        headerMulti.setPro23("Phút");
        return headerMulti;
    }

    private List<AttendanceKidsModel> setAttendanceDetail(List<AttendanceKidsDetailDateResponse> listAttendanceKidsDetailDateResponse, ListIdKidDTO listIdKidDTO) {
        List<AttendanceKidsDetailDateResponse> dateResponseList = new ArrayList<>();
        for (AttendanceKidsDetailDateResponse attendanceKidsDateResponse : listAttendanceKidsDetailDateResponse) {
            if (attendanceKidsDateResponse.getKids().getId().equals(listIdKidDTO.getId())) {
                dateResponseList.add(attendanceKidsDateResponse);
            }
        }
        List<AttendanceKidsModel> attendanceKidsModels = new ArrayList<>();
        long i = 1;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (AttendanceKidsDetailDateResponse model : dateResponseList) {
            AttendanceKidsModel attendanceKidsModel = new AttendanceKidsModel();
            attendanceKidsModel.setId(i++);

            if (model.getKids() == null) {
                continue;
            }

            String date = df.format(model.getAttendanceDate());
            attendanceKidsModel.setAttendanceDate(date);
            attendanceKidsModel.setKidName(model.getKids().getFullName());
            if (model.getAttendanceArriveKids().isEvening() || model.getAttendanceArriveKids().isEveningYes() || model.getAttendanceArriveKids().isEveningNo()) {
                if (model.getAttendanceArriveKids().isMorning() && model.getAttendanceArriveKids().isAfternoon() && model.getAttendanceArriveKids().isEvening()) {
                    attendanceKidsModel.setAbsentStatus("x");
                } else {
                    attendanceKidsModel.setAbsentStatus("");
                }
                if (model.getAttendanceArriveKids().isMorningYes() && model.getAttendanceArriveKids().isAfternoonYes() && model.getAttendanceArriveKids().isEveningYes()) {
                    attendanceKidsModel.setAbsentLetterYes("x");
                } else {
                    attendanceKidsModel.setAbsentLetterYes("");
                }
                if (model.getAttendanceArriveKids().isMorningNo() && model.getAttendanceArriveKids().isAfternoonNo() && model.getAttendanceArriveKids().isEveningNo()) {
                    attendanceKidsModel.setAbsentLetterNo("x");
                } else {
                    attendanceKidsModel.setAbsentLetterNo("");
                }
            } else {
                if (model.getAttendanceArriveKids().isMorning() && model.getAttendanceArriveKids().isAfternoon()) {
                    attendanceKidsModel.setAbsentStatus("x");
                } else {
                    attendanceKidsModel.setAbsentStatus("");
                }
                if (model.getAttendanceArriveKids().isMorningYes() && model.getAttendanceArriveKids().isAfternoonYes()) {
                    attendanceKidsModel.setAbsentLetterYes("x");
                } else {
                    attendanceKidsModel.setAbsentLetterYes("");
                }
                if (model.getAttendanceArriveKids().isMorningNo() && model.getAttendanceArriveKids().isAfternoonNo()) {
                    attendanceKidsModel.setAbsentLetterNo("x");
                } else {
                    attendanceKidsModel.setAbsentLetterNo("");
                }
            }
            if (model.getAttendanceArriveKids().isMorning()) {
                attendanceKidsModel.setMorning("x");
            } else {
                attendanceKidsModel.setMorning("");
            }
            if (model.getAttendanceArriveKids().isMorningNo()) {
                attendanceKidsModel.setMorningNo("x");
            } else {
                attendanceKidsModel.setMorningNo("");
            }
            if (model.getAttendanceArriveKids().isMorningYes()) {
                attendanceKidsModel.setMorningYes("x");
            } else {
                attendanceKidsModel.setMorningYes("");
            }
            if (model.getAttendanceArriveKids().isAfternoon()) {
                attendanceKidsModel.setAfternoon("x");
            } else {
                attendanceKidsModel.setAfternoon("");
            }

            if (model.getAttendanceArriveKids().isAfternoonNo()) {
                attendanceKidsModel.setAfternoonNo("x");
            } else {
                attendanceKidsModel.setAfternoonNo("");
            }
            if (model.getAttendanceArriveKids().isAfternoonYes()) {
                attendanceKidsModel.setAfternoonYes("x");
            } else {
                attendanceKidsModel.setAfternoonYes("");
            }
            if (model.getAttendanceArriveKids().isEvening()) {
                attendanceKidsModel.setEvening("x");
            } else {
                attendanceKidsModel.setEvening("");
            }
            if (model.getAttendanceArriveKids().isEveningNo()) {
                attendanceKidsModel.setEveningNo("x");
            } else {
                attendanceKidsModel.setEveningNo("");
            }
            if (model.getAttendanceArriveKids().isEveningYes()) {
                attendanceKidsModel.setEveningYes("x");
            } else {
                attendanceKidsModel.setEveningYes("");
            }
            if (model.getAttendanceEatKids().isBreakfast()) {
                attendanceKidsModel.setEatBreakfast("x");
            } else {
                attendanceKidsModel.setEatBreakfast("");
            }
            if (model.getAttendanceEatKids().isSecondBreakfast()) {
                attendanceKidsModel.setEatSecondBreakfast("x");
            } else {
                attendanceKidsModel.setEatSecondBreakfast("");
            }
            if (model.getAttendanceEatKids().isLunch()) {
                attendanceKidsModel.setEatLunch("x");
            } else {
                attendanceKidsModel.setEatLunch("");
            }
            if (model.getAttendanceEatKids().isAfternoon()) {
                attendanceKidsModel.setEatAfternoon("x");
            } else {
                attendanceKidsModel.setEatAfternoon("");
            }
            if (model.getAttendanceEatKids().isSecondAfternoon()) {
                attendanceKidsModel.setEatSecondAfternoon("x");
            } else {
                attendanceKidsModel.setEatSecondAfternoon("");
            }
            if (model.getAttendanceEatKids().isDinner()) {
                attendanceKidsModel.setEatDinner("x");
            } else {
                attendanceKidsModel.setEatDinner("");
            }
            if (model.getAttendanceArriveKids().getTimeArriveKid() != null) {
                String timeArriveKid = model.getAttendanceArriveKids().getTimeArriveKid().format(dtf);
                attendanceKidsModel.setTimeArriveKid(timeArriveKid);
            } else {
                attendanceKidsModel.setTimeArriveKid("");
            }
            if (model.getAttendanceLeaveKids().getTimeLeaveKid() != null) {
                String timeLeaveKid = model.getAttendanceLeaveKids().getTimeLeaveKid().format(dtf);
                attendanceKidsModel.setTimeLeaveKid(timeLeaveKid);
            } else {
                attendanceKidsModel.setTimeLeaveKid("");
            }
            if (model.getAttendanceLeaveKids().getMinutePickupLate() != 0) {
                String mutePickupLate = String.valueOf(model.getAttendanceLeaveKids().getMinutePickupLate());
                attendanceKidsModel.setMinutePickupLate(mutePickupLate);
            } else {
                attendanceKidsModel.setMinutePickupLate("");
            }
            attendanceKidsModels.add(attendanceKidsModel);

        }
        return attendanceKidsModels;
    }

    private List<AttendanceKidsModel> setAttendanceKidsModel(ListAttendanceKidsDetailDateResponse data) {
        List<AttendanceKidsModel> attendanceKidsModels = new ArrayList<>();
        long i = 1;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        List<AttendanceKidsDetailDateResponse> dataAttendanceKidList = data.getAttendanceDetailDateList();

        for (AttendanceKidsDetailDateResponse model : dataAttendanceKidList) {
            AttendanceKidsModel attendanceKidsModel = new AttendanceKidsModel();
            attendanceKidsModel.setId(i++);

            if (model.getKids().getFullName() != null) {
                attendanceKidsModel.setKidName(model.getKids().getFullName());
            } else {
                attendanceKidsModel.setKidName("");
            }
            if (model.getAttendanceArriveKids().isEvening() || model.getAttendanceArriveKids().isEveningYes() || model.getAttendanceArriveKids().isEveningNo()) {
                if (model.getAttendanceArriveKids().isMorning() && model.getAttendanceArriveKids().isAfternoon() && model.getAttendanceArriveKids().isEvening()) {
                    attendanceKidsModel.setAbsentStatus("x");
                } else {
                    attendanceKidsModel.setAbsentStatus("");
                }
                if (model.getAttendanceArriveKids().isMorningYes() && model.getAttendanceArriveKids().isAfternoonYes() && model.getAttendanceArriveKids().isEveningYes()) {
                    attendanceKidsModel.setAbsentLetterYes("x");
                } else {
                    attendanceKidsModel.setAbsentLetterYes("");
                }
                if (model.getAttendanceArriveKids().isMorningNo() && model.getAttendanceArriveKids().isAfternoonNo() && model.getAttendanceArriveKids().isEveningNo()) {
                    attendanceKidsModel.setAbsentLetterNo("x");
                } else {
                    attendanceKidsModel.setAbsentLetterNo("");
                }
            } else {
                if (model.getAttendanceArriveKids().isMorning() && model.getAttendanceArriveKids().isAfternoon()) {
                    attendanceKidsModel.setAbsentStatus("x");
                } else {
                    attendanceKidsModel.setAbsentStatus("");
                }
                if (model.getAttendanceArriveKids().isMorningYes() && model.getAttendanceArriveKids().isAfternoonYes()) {
                    attendanceKidsModel.setAbsentLetterYes("x");
                } else {
                    attendanceKidsModel.setAbsentLetterYes("");
                }
                if (model.getAttendanceArriveKids().isMorningNo() && model.getAttendanceArriveKids().isAfternoonNo()) {
                    attendanceKidsModel.setAbsentLetterNo("x");
                } else {
                    attendanceKidsModel.setAbsentLetterNo("");
                }
            }
            if (model.getAttendanceArriveKids().isMorning()) {
                attendanceKidsModel.setMorning("x");
            } else {
                attendanceKidsModel.setMorning("");
            }
            if (model.getAttendanceArriveKids().isMorningNo()) {
                attendanceKidsModel.setMorningNo("x");
            } else {
                attendanceKidsModel.setMorningNo("");
            }
            if (model.getAttendanceArriveKids().isMorningYes()) {
                attendanceKidsModel.setMorningYes("x");
            } else {
                attendanceKidsModel.setMorningYes("");
            }
            if (model.getAttendanceArriveKids().isAfternoon()) {
                attendanceKidsModel.setAfternoon("x");
            } else {
                attendanceKidsModel.setAfternoon("");
            }

            if (model.getAttendanceArriveKids().isAfternoonNo()) {
                attendanceKidsModel.setAfternoonNo("x");
            } else {
                attendanceKidsModel.setAfternoonNo("");
            }
            if (model.getAttendanceArriveKids().isAfternoonYes()) {
                attendanceKidsModel.setAfternoonYes("x");
            } else {
                attendanceKidsModel.setAfternoonYes("");
            }
            if (model.getAttendanceArriveKids().isEvening()) {
                attendanceKidsModel.setEvening("x");
            } else {
                attendanceKidsModel.setEvening("");
            }
            if (model.getAttendanceArriveKids().isEveningNo()) {
                attendanceKidsModel.setEveningNo("x");
            } else {
                attendanceKidsModel.setEveningNo("");
            }
            if (model.getAttendanceArriveKids().isEveningYes()) {
                attendanceKidsModel.setEveningYes("x");
            } else {
                attendanceKidsModel.setEveningYes("");
            }
            if (model.getAttendanceEatKids().isBreakfast()) {
                attendanceKidsModel.setEatBreakfast("x");
            } else {
                attendanceKidsModel.setEatBreakfast("");
            }
            if (model.getAttendanceEatKids().isSecondBreakfast()) {
                attendanceKidsModel.setEatSecondBreakfast("x");
            } else {
                attendanceKidsModel.setEatSecondBreakfast("");
            }
            if (model.getAttendanceEatKids().isLunch()) {
                attendanceKidsModel.setEatLunch("x");
            } else {
                attendanceKidsModel.setEatLunch("");
            }
            if (model.getAttendanceEatKids().isAfternoon()) {
                attendanceKidsModel.setEatAfternoon("x");
            } else {
                attendanceKidsModel.setEatAfternoon("");
            }
            if (model.getAttendanceEatKids().isSecondAfternoon()) {
                attendanceKidsModel.setEatSecondAfternoon("x");
            } else {
                attendanceKidsModel.setEatSecondAfternoon("");
            }
            if (model.getAttendanceEatKids().isDinner()) {
                attendanceKidsModel.setEatDinner("x");
            } else {
                attendanceKidsModel.setEatDinner("");
            }
            if (model.getAttendanceArriveKids().getTimeArriveKid() != null) {
                String timeArriveKid = model.getAttendanceArriveKids().getTimeArriveKid().format(dtf);
                attendanceKidsModel.setTimeArriveKid(timeArriveKid);
            } else {
                attendanceKidsModel.setTimeArriveKid("");
            }
            if (model.getAttendanceLeaveKids().getTimeLeaveKid() != null) {
                String timeLeaveKid = model.getAttendanceLeaveKids().getTimeLeaveKid().format(dtf);
                attendanceKidsModel.setTimeLeaveKid(timeLeaveKid);
            } else {
                attendanceKidsModel.setTimeLeaveKid("");
            }
            if (model.getAttendanceLeaveKids().getMinutePickupLate() != 0) {
                String mutePickupLate = String.valueOf(model.getAttendanceLeaveKids().getMinutePickupLate());
                attendanceKidsModel.setMinutePickupLate(mutePickupLate);
            } else {
                attendanceKidsModel.setMinutePickupLate("");
            }
            attendanceKidsModels.add(attendanceKidsModel);

        }
        return attendanceKidsModels;
    }

//    @Transactional
//    @Override
//    public void approveAbsentLetter(AbsentLetter absentLetter) {
//        LocalDate startDate = absentLetter.getFromDate();
//        LocalDate endDate = absentLetter.getToDate();
//        Long idKid = absentLetter.getKids().getId();
//        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findAttendanceKidStartEndDate(idKid, startDate, endDate);
//        attendanceKidsList.forEach(x -> {
//            x.setStatusNumber(2);
//            attendanceKidsRepository.save(x);
//        });
//    }

    /**
     * set giá trị có điểm danh đến hay chưa
     *
     * @param arriveRequest
     * @return
     */
    private boolean getAttendanceArriveStastus(AttendanceArriveKidsRequest arriveRequest) {
        return arriveRequest.isMorning() || arriveRequest.isMorningYes() || arriveRequest.isMorningNo() || arriveRequest.isAfternoon() || arriveRequest.isAfternoonYes() || arriveRequest.isAfternoonNo() || arriveRequest.isEvening() || arriveRequest.isEveningYes() || arriveRequest.isEveningNo();
    }

    /**
     * set giá trị có điểm danh đến hay chưa
     *
     * @param leaveRequest
     * @return
     */
    private boolean getAttendanceLeaveStastus(AttendanceLeaveKidsRequest leaveRequest) {
        return leaveRequest.isStatusLeave() ? AppConstant.APP_TRUE : AppConstant.APP_FALSE;
    }

    /**
     * set giá trị có điểm danh ăn hay chưa
     *
     * @param eatRequest
     * @return
     */
    private boolean getAttendanceEatStastus(AttendanceEatKidsRequest eatRequest) {
        return eatRequest.isBreakfast() || eatRequest.isSecondBreakfast() || eatRequest.isLunch() || eatRequest.isAfternoon() || eatRequest.isSecondAfternoon() || eatRequest.isDinner();
    }

    /**
     * check điểm danh về
     *
     * @param attendanceKids
     * @param request
     */
    private void checkAttendanceLeave(AttendanceKids attendanceKids, AttendanceLeaveKidsDateRequest request) {
        if (!attendanceKids.isAttendanceArrive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học sinh chưa được điểm danh đến");
        } else {
            if (!attendanceKids.getAttendanceArriveKids().isMorning() && !attendanceKids.getAttendanceArriveKids().isAfternoon() && !attendanceKids.getAttendanceArriveKids().isEvening()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học sinh phải đi học ít nhất một buổi");
            }
        }
        if (request.getAttendanceLeaveKids().isStatusLeave() && request.getAttendanceLeaveKids().getTimeLeaveKid() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chưa chọn giờ về");
        }
    }

    /**
     * lấy config điểm danh
     *
     * @param idSchool
     * @param attendanceKidsList
     * @param dataRequest
     * @return
     */
    private AttendanceConfigResponse getAttendanceConfig(Long idSchool, List<AttendanceKids> attendanceKidsList, LocalDate dataRequest) {
        AttendanceConfig attendanceConfig;
        LocalDate date;
        if (CollectionUtils.isEmpty(attendanceKidsList)) {
            attendanceConfig = attendanceConfigRepository.findAttendanceConfigFinal(idSchool).orElseThrow(() -> new NotFoundException("not foud attendanceDateConfig by id in attendance"));
            date = dataRequest;
        } else {
            AttendanceKids attendanceKids = attendanceKidsList.get(0);
            attendanceConfig = attendanceKids.getAttendanceConfig();
            date = attendanceKids.getAttendanceDate();
        }
        return ConvertData.convertAttendanceConfig(attendanceConfig, date);
    }

    /**
     * reset cho điểm danh về, trường hợp điểm danh đến chuyển thàn chưa điểm danh, nghỉ có phép, nghỉ không phép cả ngày
     *
     * @param attendanceKids
     */
    private void resetAttendanceLeaveKids(AttendanceKids attendanceKids) {
        if (!attendanceKids.getAttendanceArriveKids().isMorning() && !attendanceKids.getAttendanceArriveKids().isAfternoon() && !attendanceKids.getAttendanceArriveKids().isEvening()) {
            attendanceKids.setAttendanceLeave(AppConstant.APP_FALSE);
            attendanceKids.getAttendanceLeaveKids().setStatusLeave(AppConstant.APP_FALSE);
            attendanceKids.getAttendanceLeaveKids().setTimeLeaveKid(null);
            attendanceKids.getAttendanceLeaveKids().setLeaveContent("");
            attendanceKids.getAttendanceLeaveKids().setMinutePickupLate(0);
        }
    }

}
