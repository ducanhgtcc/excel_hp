package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.entity.sample.AttendanceSample;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.response.AttendanceStatusDayResponse;
import com.example.onekids_project.mobile.teacher.request.attendacekids.*;
import com.example.onekids_project.mobile.teacher.response.attendancekids.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AttendanceKidsTeacherService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.attendancekids.AttendanceConfigResponse;
import com.example.onekids_project.response.attendancekids.StatusAttendanceDay;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.service.servicecustom.attendancekids.AttendanceKidsService;
import com.example.onekids_project.util.AttendanceKidsUtil;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceKidsTeacherServiceImpl implements AttendanceKidsTeacherService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private WebSystemTitleService webSystemTitleService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private ListMapper ListMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    AttendanceArriveKidsRepository attendanceArriveKidsRepository;

    @Autowired
    AttendanceLeaveKidsRepository attendanceLeaveKidsRepository;

    @Autowired
    AttendanceEatKidsRepository attendanceEatKidsRepository;

    @Autowired
    private AttendanceSampleRepository attendanceSampleRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Autowired
    private AttendanceKidsService attendanceKidsService;


    @Override
    public AttendanceDayTeacherResponse searchAttendanceKids(UserPrincipal principal, LocalDate localDate) {

        AttendanceDayTeacherResponse attendanceDayTeacherResponse = new AttendanceDayTeacherResponse();
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidDay(principal, localDate);

        if (attendanceKids.size() > 0) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), localDate);
            List<AttendanceKids> attendance = new ArrayList<>();
            List<AttendanceKids> attendanceOff = new ArrayList<>();
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return attendanceDayTeacherResponse;
            } else {
                attendanceKids.forEach(x -> {
                    if (x.isAttendanceArrive()) {
                        attendance.add(x);
                    }
                });

                attendanceDayTeacherResponse.setSumKid(attendanceKids.size());
                attendanceDayTeacherResponse.setSumKidAttendance(attendance.size());
                attendanceDayTeacherResponse.setSumKidNoAttendance(attendanceKids.size() - attendance.size());

                attendance.forEach(day -> {
                    if (day.isAttendanceArrive()) {
                        if (!(day.getAttendanceArriveKids().isMorning()) && !(day.getAttendanceArriveKids().isAfternoon()) && !(day.getAttendanceArriveKids().isEvening())) {
                            attendanceOff.add(day);
                        }
                    }
                });
                attendanceDayTeacherResponse.setSumKidOff(attendanceOff.size());
                return attendanceDayTeacherResponse;
            }
        } else {
            return attendanceDayTeacherResponse;
        }
    }

    @Override
    public List<AttendanceKidsArriveTeacherResponse> searchAttendanceKidsDetail(UserPrincipal principal, LocalDate localDate) {

        List<AttendanceKidsArriveTeacherResponse> dataList = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
        String checkNull = "";

        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(localDate, principal.getIdClassLogin());
        if (CollectionUtils.isEmpty(kidsList)) {
            return dataList;
        }
        List<Long> idKidList = kidsList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidsDateList(idKidList, localDate);

        if (!CollectionUtils.isEmpty(attendanceKids)) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), localDate);
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataList;
            } else {
                attendanceKids.forEach(attendance -> {
                    AttendanceKidsArriveTeacherResponse data = new AttendanceKidsArriveTeacherResponse();
                    data.setId(attendance.getAttendanceArriveKids().getId());
                    data.setIdKid(attendance.getKids().getId());
                    data.setKidName(attendance.getKids().getFullName());
                    data.setNickName(StringUtils.isNotBlank(attendance.getKids().getNickName()) ? attendance.getKids().getNickName() : "");
                    data.setStatus(attendance.isAttendanceArrive());

                    data.setAvatar(ConvertData.getAvatarKid(attendance.getKids()));
                    if (attendance.getAttendanceArriveKids().getArriveUrlPicture() != null) {
                        data.setPicture(attendance.getAttendanceArriveKids().getArriveUrlPicture());
                    } else {
                        data.setPicture(checkNull);
                    }
                    if (attendance.getAttendanceArriveKids().getArriveContent() != null) {
                        data.setContent(attendance.getAttendanceArriveKids().getArriveContent());
                    } else {

                        data.setContent(checkNull);
                    }
                    if (attendance.getAttendanceArriveKids().getTimeArriveKid() != null) {
                        data.setTime(attendance.getAttendanceArriveKids().getTimeArriveKid().format(df));
                    } else {
                        data.setTime(checkNull);
                    }
                    if (attendanceConfigResponse.isMorningAttendanceArrive()) {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        attendanceStatusDayResponse.setAbsentLetter(attendance.getAttendanceArriveKids().isMorning());
                        attendanceStatusDayResponse.setAbsentLetterYes(attendance.getAttendanceArriveKids().isMorningYes());
                        attendanceStatusDayResponse.setAbsentLetterNo(attendance.getAttendanceArriveKids().isMorningNo());
                        data.setMorningList(attendanceStatusDayResponse);

                    } else {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        data.setMorningList(attendanceStatusDayResponse);
                    }
                    if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        attendanceStatusDayResponse.setAbsentLetter(attendance.getAttendanceArriveKids().isAfternoon());
                        attendanceStatusDayResponse.setAbsentLetterYes(attendance.getAttendanceArriveKids().isAfternoonYes());
                        attendanceStatusDayResponse.setAbsentLetterNo(attendance.getAttendanceArriveKids().isAfternoonNo());
                        data.setAfternoonList(attendanceStatusDayResponse);
                    } else {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        data.setAfternoonList(attendanceStatusDayResponse);
                    }
                    if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        attendanceStatusDayResponse.setAbsentLetter(attendance.getAttendanceArriveKids().isEvening());
                        attendanceStatusDayResponse.setAbsentLetterYes(attendance.getAttendanceArriveKids().isEveningYes());
                        attendanceStatusDayResponse.setAbsentLetterNo(attendance.getAttendanceArriveKids().isEveningNo());
                        data.setEveningList(attendanceStatusDayResponse);
                    } else {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        data.setEveningList(attendanceStatusDayResponse);
                    }

                    dataList.add(data);
                });
            }
            return dataList;
        } else {
            return dataList;
        }
    }

    @Transactional
    @Override
    public AiAttendanceKidsArriveTeacherResponse createAiAttendanceKidsTeacher(UserPrincipal principal, AiAttendanceKidArriveTeacherRequest aiRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        Long idKid = aiRequest.getId();
        Long idClass = principal.getIdClassLogin();
        LocalDate date = LocalDate.now();
        AiAttendanceKidsArriveTeacherResponse dataResult = new AiAttendanceKidsArriveTeacherResponse();
        AttendanceKids attendanceKids = attendanceKidsRepository.findByDelActiveTrueAndAttendanceDateAndKids_IdAndMaClass_Id(date, idKid, idClass);
        if (attendanceKids == null) {
            dataResult.setStatus(AppConstant.APP_FALSE);
            return dataResult;
        }
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.getAttendanceConfig(), date);
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            dataResult.setStatus(AppConstant.APP_FALSE);
            return dataResult;
        } else {
            AttendanceArriveKids attendanceArriveKids = attendanceKids.getAttendanceArriveKids();
            boolean checkFireBase = attendanceArriveKids.getIdCreated() == null;
            attendanceArriveKids.setArriveContent(aiRequest.getContent());
            if (aiRequest.getPicture() != null) {
                HandleFileResponse handleFileResponse = null;
                try {
                    handleFileResponse = HandleFileUtils.getUrlPictureSaved(aiRequest.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String urlWeb = handleFileResponse.getUrlWeb();
                String urlLocal = handleFileResponse.getUrlLocal();
                attendanceArriveKids.setArriveUrlPicture(urlWeb);
                attendanceArriveKids.setArriveUrlPictureLocal(urlLocal);
            }


            if (attendanceConfigResponse.isMorningAttendanceArrive() && aiRequest.getMorningList() != null) {
                attendanceArriveKids.setMorning(aiRequest.getMorningList().isAbsentLetter());
                attendanceArriveKids.setMorningYes(aiRequest.getMorningList().isAbsentLetterYes());
                attendanceArriveKids.setMorningNo(aiRequest.getMorningList().isAbsentLetterNo());
            }
            if (attendanceConfigResponse.isAfternoonAttendanceArrive() && aiRequest.getAfternoonList() != null) {
                attendanceArriveKids.setAfternoon(aiRequest.getAfternoonList().isAbsentLetter());
                attendanceArriveKids.setAfternoonYes(aiRequest.getAfternoonList().isAbsentLetterYes());
                attendanceArriveKids.setAfternoonNo(aiRequest.getAfternoonList().isAbsentLetterNo());
            }
            if (attendanceConfigResponse.isEveningAttendanceArrive() && aiRequest.getEveningList() != null) {
                attendanceArriveKids.setEvening(aiRequest.getEveningList().isAbsentLetter());
                attendanceArriveKids.setEveningYes(aiRequest.getEveningList().isAbsentLetterYes());
                attendanceArriveKids.setEveningNo(aiRequest.getEveningList().isAbsentLetterNo());
            }

            if (attendanceArriveKids.isMorning() || attendanceArriveKids.isAfternoon() || attendanceArriveKids.isEvening()) {
                LocalTime time = LocalTime.parse(ConvertData.convertTimeHHMM(LocalTime.now()));
                attendanceArriveKids.setTimeArriveKid(time);
            } else {
                attendanceArriveKids.setTimeArriveKid(null);
            }
            AttendanceArriveKids attendanceArriveKidsSave = attendanceArriveKidsRepository.save(attendanceArriveKids);
            attendanceKids.setAttendanceArriveKids(attendanceArriveKidsSave);
            attendanceKids.setAttendanceArrive(AttendanceKidsUtil.checkArriveHas(attendanceArriveKidsSave));
            boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_ARRIVE, principal, attendanceKids);
            AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKids);
            //tự động điểm danh ăn theo config
            attendanceKidsService.saveAttendanceEatAuto(attendanceKidsSaved);
            if (checkSendFirebase) {
                long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.TEACHER);
                if (idWebSystem != 0) {
                    //send firebase
                    firebaseFunctionService.sendParentByTeacherNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE, "");
                }
            }
        }
        dataResult.setStatus(AppConstant.APP_TRUE);
        return dataResult;
    }

    @Transactional
    @Override
    public List<AttendanceKidsArriveTeacherResponse> createAttendanceKidsTeacher(UserPrincipal principal, AttendanceKidArriveTeacherRequest attendanceKidArriveTeacherRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        Long idArrive = attendanceKidArriveTeacherRequest.getIdAttendace().get(0);
        AttendanceArriveKids attendanceArriveKidsOne = attendanceArriveKidsRepository.findByIdAndDelActiveTrue(idArrive);
        LocalDate date = attendanceArriveKidsOne.getAttendanceKids().getAttendanceDate();
        // ngày điểm danh phải lớn hơn ngày config
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendace(date, principal.getSchoolConfig().getAgainAttendance());
        }

        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidDay(principal, date);
        List<AttendanceKidsArriveTeacherResponse> dataResponse = new ArrayList<>();
        if (attendanceKids.size() > 0) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), date);
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataResponse;
            } else {

                for (Long x : attendanceKidArriveTeacherRequest.getIdAttendace()) {
                    AttendanceKidsArriveTeacherResponse dataKidResponse = new AttendanceKidsArriveTeacherResponse();

                    AttendanceArriveKids attendanceArriveKidNew = attendanceArriveKidsRepository.findByIdAndDelActiveTrue(x);
                    boolean checkFireBase = attendanceArriveKidNew.getIdCreated() == null;
                    AttendanceKids attendanceKidNew = attendanceArriveKidNew.getAttendanceKids();

                    attendanceArriveKidNew.setArriveContent(attendanceKidArriveTeacherRequest.getContent());

                    if (attendanceKidArriveTeacherRequest.getPicture() != null) {
                        HandleFileResponse handleFileResponse = null;
                        try {
                            handleFileResponse = HandleFileUtils.getUrlPictureSaved(attendanceKidArriveTeacherRequest.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String urlWeb = handleFileResponse.getUrlWeb();
                        String urlLocal = handleFileResponse.getUrlLocal();
                        attendanceArriveKidNew.setArriveUrlPicture(urlWeb);
                        attendanceArriveKidNew.setArriveUrlPictureLocal(urlLocal);
                    } else {
                        if (attendanceKidArriveTeacherRequest.isDeletePicture()) {
                            attendanceArriveKidNew.setArriveUrlPicture(null);
                        }
                    }


                    if (attendanceConfigResponse.isMorningAttendanceArrive() && attendanceKidArriveTeacherRequest.getMorningList() != null) {
                        attendanceArriveKidNew.setMorning(attendanceKidArriveTeacherRequest.getMorningList().isAbsentLetter());
                        attendanceArriveKidNew.setMorningYes(attendanceKidArriveTeacherRequest.getMorningList().isAbsentLetterYes());
                        attendanceArriveKidNew.setMorningNo(attendanceKidArriveTeacherRequest.getMorningList().isAbsentLetterNo());
                    }
                    if (attendanceConfigResponse.isAfternoonAttendanceArrive() && attendanceKidArriveTeacherRequest.getAfternoonList() != null) {
                        attendanceArriveKidNew.setAfternoon(attendanceKidArriveTeacherRequest.getAfternoonList().isAbsentLetter());
                        attendanceArriveKidNew.setAfternoonYes(attendanceKidArriveTeacherRequest.getAfternoonList().isAbsentLetterYes());
                        attendanceArriveKidNew.setAfternoonNo(attendanceKidArriveTeacherRequest.getAfternoonList().isAbsentLetterNo());
                    }
                    if (attendanceConfigResponse.isEveningAttendanceArrive() && attendanceKidArriveTeacherRequest.getEveningList() != null) {
                        attendanceArriveKidNew.setEvening(attendanceKidArriveTeacherRequest.getEveningList().isAbsentLetter());
                        attendanceArriveKidNew.setEveningYes(attendanceKidArriveTeacherRequest.getEveningList().isAbsentLetterYes());
                        attendanceArriveKidNew.setEveningNo(attendanceKidArriveTeacherRequest.getEveningList().isAbsentLetterNo());
                    }
                    if (attendanceArriveKidNew.isMorning() || attendanceArriveKidNew.isAfternoon() || attendanceArriveKidNew.isEvening()) {
                        if (!Strings.isEmpty(attendanceKidArriveTeacherRequest.getTime())) {
                            LocalTime time = LocalTime.parse(attendanceKidArriveTeacherRequest.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
                            attendanceArriveKidNew.setTimeArriveKid(time);
                        } else {

                            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                            String timeNew = df.format(LocalTime.now());
                            LocalTime time = LocalTime.parse(timeNew);

                            if (attendanceArriveKidNew.getTimeArriveKid() == null) {
                                attendanceArriveKidNew.setTimeArriveKid(time);
                            }

                        }
                    } else {
                        attendanceArriveKidNew.setTimeArriveKid(null);
                    }

                    attendanceKidNew.setAttendanceArrive(AttendanceKidsUtil.checkArriveHas(attendanceArriveKidNew));
                    boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_ARRIVE, principal, attendanceKidNew);
                    AttendanceArriveKids attendanceArriveKids = attendanceArriveKidsRepository.save(attendanceArriveKidNew);
                    attendanceKidNew.setAttendanceArriveKids(attendanceArriveKids);
                    AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKidNew);
                    //tự động điểm danh ăn theo config
                    attendanceKidsService.saveAttendanceEatAuto(attendanceKidsSaved);
                    if (checkSendFirebase) {
                        long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.TEACHER);
                        if (idWebSystem != 0) {
                            //send firebase
                            firebaseFunctionService.sendParentByTeacherNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE, "");
                        }
                    }
                    //du liệu trả về
                    if (attendanceConfigResponse.isMorningAttendanceArrive()) {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        attendanceStatusDayResponse.setAbsentLetter(attendanceArriveKids.isMorning());
                        attendanceStatusDayResponse.setAbsentLetterYes(attendanceArriveKids.isMorningYes());
                        attendanceStatusDayResponse.setAbsentLetterNo(attendanceArriveKids.isMorningNo());
                        dataKidResponse.setMorningList(attendanceStatusDayResponse);

                    } else {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        dataKidResponse.setMorningList(attendanceStatusDayResponse);
                    }
                    if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        attendanceStatusDayResponse.setAbsentLetter(attendanceArriveKids.isAfternoon());
                        attendanceStatusDayResponse.setAbsentLetterYes(attendanceArriveKids.isAfternoonYes());
                        attendanceStatusDayResponse.setAbsentLetterNo(attendanceArriveKids.isAfternoonNo());
                        dataKidResponse.setAfternoonList(attendanceStatusDayResponse);
                    } else {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        dataKidResponse.setAfternoonList(attendanceStatusDayResponse);
                    }
                    if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        attendanceStatusDayResponse.setAbsentLetter(attendanceArriveKids.isEvening());
                        attendanceStatusDayResponse.setAbsentLetterYes(attendanceArriveKids.isEveningYes());
                        attendanceStatusDayResponse.setAbsentLetterNo(attendanceArriveKids.isEveningNo());
                        dataKidResponse.setEveningList(attendanceStatusDayResponse);
                    } else {
                        AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                        dataKidResponse.setEveningList(attendanceStatusDayResponse);
                    }

                    if (attendanceArriveKids.getTimeArriveKid() != null) {
                        dataKidResponse.setTime(attendanceArriveKids.getTimeArriveKid().toString());
                    } else {
                        dataKidResponse.setTime("");
                    }
                    if (attendanceArriveKids.getArriveUrlPicture() != null) {
                        dataKidResponse.setPicture(attendanceArriveKids.getArriveUrlPicture());
                    } else {
                        dataKidResponse.setPicture("");
                    }

                    dataKidResponse.setAvatar(ConvertData.getAvatarKid(attendanceKidsSaved.getKids()));

                    dataKidResponse.setStatus(attendanceKidsSaved.isAttendanceArrive());

                    if (attendanceKidsSaved.getKids().getFullName() != null) {
                        dataKidResponse.setKidName(attendanceKidsSaved.getKids().getFullName());
                    } else {
                        dataKidResponse.setKidName("");
                    }

                    if (attendanceArriveKids.getArriveContent() != null) {
                        dataKidResponse.setContent(attendanceArriveKids.getArriveContent());
                    } else {
                        dataKidResponse.setContent("");
                    }
                    dataKidResponse.setId(x);
                    dataResponse.add(dataKidResponse);
                }
                return dataResponse;
            }
        } else {
            return dataResponse;
        }
    }

    @Transactional
    @Override
    public List<AttendanceKidsArriveTeacherResponse> createAttendanceKidsMultiTeacher(UserPrincipal principal, AttendanceKidArriveTeacherMultiRequest attendanceKidArriveList) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        List<AttendanceKidsArriveTeacherResponse> dataList = new ArrayList<>();

        //checkconfig ngày điểm danh
        Long idArrive = attendanceKidArriveList.getAttendanceOneKidArriveList().get(0).getId();
        AttendanceArriveKids attendanceArriveKidsOne = attendanceArriveKidsRepository.findByIdAndDelActiveTrue(idArrive);
        if (attendanceArriveKidsOne != null) {
            LocalDate date = attendanceArriveKidsOne.getAttendanceKids().getAttendanceDate();
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceArriveKidsOne.getAttendanceKids().getAttendanceConfig(), date);
            // ngày điểm danh phải lớn hơn ngày config
            if (principal.getSchoolConfig().getAgainAttendance() != null) {
                this.checkAgainAttendace(date, principal.getSchoolConfig().getAgainAttendance());
            }

            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataList;
            }

            for (AttendanceOneKidArriveTeacherRequest attendanceKidArriveTeacherRequest : attendanceKidArriveList.getAttendanceOneKidArriveList()) {
                Long id = attendanceKidArriveTeacherRequest.getId();
                AttendanceKidsArriveTeacherResponse dataKidResponse = new AttendanceKidsArriveTeacherResponse();

                AttendanceArriveKids attendanceArriveKidNew = attendanceArriveKidsRepository.findByIdAndDelActiveTrue(id);
                boolean checkFireBase = attendanceArriveKidNew.getIdCreated() == null;
                AttendanceKids attendanceKidNew = attendanceArriveKidNew.getAttendanceKids();

                attendanceArriveKidNew.setArriveContent(attendanceKidArriveTeacherRequest.getContent());

                if (attendanceKidArriveTeacherRequest.getPicture() != null) {
                    HandleFileResponse handleFileResponse = null;
                    try {
                        handleFileResponse = HandleFileUtils.getUrlPictureSaved(attendanceKidArriveTeacherRequest.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String urlWeb = handleFileResponse.getUrlWeb();
                    String urlLocal = handleFileResponse.getUrlLocal();
                    attendanceArriveKidNew.setArriveUrlPicture(urlWeb);
                    attendanceArriveKidNew.setArriveUrlPictureLocal(urlLocal);
                } else {
                    if (attendanceKidArriveTeacherRequest.isDeletePicture()) {
                        attendanceArriveKidNew.setArriveUrlPicture(null);
                    }
                }


                if (attendanceConfigResponse.isMorningAttendanceArrive() && attendanceKidArriveTeacherRequest.getMorningList() != null) {
                    attendanceArriveKidNew.setMorning(attendanceKidArriveTeacherRequest.getMorningList().isAbsentLetter());
                    attendanceArriveKidNew.setMorningYes(attendanceKidArriveTeacherRequest.getMorningList().isAbsentLetterYes());
                    attendanceArriveKidNew.setMorningNo(attendanceKidArriveTeacherRequest.getMorningList().isAbsentLetterNo());
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive() && attendanceKidArriveTeacherRequest.getAfternoonList() != null) {
                    attendanceArriveKidNew.setAfternoon(attendanceKidArriveTeacherRequest.getAfternoonList().isAbsentLetter());
                    attendanceArriveKidNew.setAfternoonYes(attendanceKidArriveTeacherRequest.getAfternoonList().isAbsentLetterYes());
                    attendanceArriveKidNew.setAfternoonNo(attendanceKidArriveTeacherRequest.getAfternoonList().isAbsentLetterNo());
                }
                logger.info("show attendance: {}, {}", attendanceConfigResponse.isEveningAttendanceArrive(), attendanceKidArriveTeacherRequest.getEveningList());
                if (attendanceConfigResponse.isEveningAttendanceArrive() && attendanceKidArriveTeacherRequest.getEveningList() != null) {
                    attendanceArriveKidNew.setEvening(attendanceKidArriveTeacherRequest.getEveningList().isAbsentLetter());
                    attendanceArriveKidNew.setEveningYes(attendanceKidArriveTeacherRequest.getEveningList().isAbsentLetterYes());
                    attendanceArriveKidNew.setEveningNo(attendanceKidArriveTeacherRequest.getEveningList().isAbsentLetterNo());
                }
                if (attendanceArriveKidNew.isMorning() || attendanceArriveKidNew.isAfternoon() || attendanceArriveKidNew.isEvening()) {
                    if (!Strings.isEmpty(attendanceKidArriveTeacherRequest.getTime())) {
                        LocalTime time = LocalTime.parse(attendanceKidArriveTeacherRequest.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
                        attendanceArriveKidNew.setTimeArriveKid(time);
                    } else {

                        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                        String timeNew = df.format(LocalTime.now());
                        LocalTime time = LocalTime.parse(timeNew);

                        if (attendanceArriveKidNew.getTimeArriveKid() == null) {
                            attendanceArriveKidNew.setTimeArriveKid(time);
                        }
                    }
                } else {
                    attendanceArriveKidNew.setTimeArriveKid(null);
                }
                AttendanceArriveKids attendanceArriveKids = attendanceArriveKidsRepository.save(attendanceArriveKidNew);
                attendanceKidNew.setAttendanceArrive(AttendanceKidsUtil.checkArriveHas(attendanceArriveKids));
                boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_ARRIVE, principal, attendanceKidNew);
                attendanceKidNew.setAttendanceArriveKids(attendanceArriveKids);
                AttendanceKids attendanceKidsSaved = attendanceKidsRepository.save(attendanceKidNew);
                //tự động điểm danh ăn theo config
                attendanceKidsService.saveAttendanceEatAuto(attendanceKidsSaved);
                if (checkSendFirebase) {
                    long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSaved, AttendanceConstant.ATTENDANCE_ARRIVE, AppTypeConstant.TEACHER);
                    if (idWebSystem != 0) {
                        //send firebase
                        firebaseFunctionService.sendParentByTeacherNoContent(idWebSystem, attendanceKidsSaved.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE, "");
                    }
                }
                //du liệu trả về
                if (attendanceConfigResponse.isMorningAttendanceArrive()) {

                    AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                    attendanceStatusDayResponse.setAbsentLetter(attendanceArriveKids.isMorning());
                    attendanceStatusDayResponse.setAbsentLetterYes(attendanceArriveKids.isMorningYes());
                    attendanceStatusDayResponse.setAbsentLetterNo(attendanceArriveKids.isMorningNo());
                    dataKidResponse.setMorningList(attendanceStatusDayResponse);

                } else {
                    AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                    dataKidResponse.setMorningList(attendanceStatusDayResponse);
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                    AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                    attendanceStatusDayResponse.setAbsentLetter(attendanceArriveKids.isAfternoon());
                    attendanceStatusDayResponse.setAbsentLetterYes(attendanceArriveKids.isAfternoonYes());
                    attendanceStatusDayResponse.setAbsentLetterNo(attendanceArriveKids.isAfternoonNo());
                    dataKidResponse.setAfternoonList(attendanceStatusDayResponse);
                } else {
                    AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                    dataKidResponse.setAfternoonList(attendanceStatusDayResponse);
                }
                if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                    AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                    attendanceStatusDayResponse.setAbsentLetter(attendanceArriveKids.isEvening());
                    attendanceStatusDayResponse.setAbsentLetterYes(attendanceArriveKids.isEveningYes());
                    attendanceStatusDayResponse.setAbsentLetterNo(attendanceArriveKids.isEveningNo());
                    dataKidResponse.setEveningList(attendanceStatusDayResponse);
                } else {
                    AttendanceStatusDayResponse attendanceStatusDayResponse = new AttendanceStatusDayResponse();
                    dataKidResponse.setEveningList(attendanceStatusDayResponse);
                }

                if (attendanceArriveKids.getTimeArriveKid() != null) {
                    dataKidResponse.setTime(attendanceArriveKids.getTimeArriveKid().toString());
                } else {
                    dataKidResponse.setTime("");
                }
                if (attendanceArriveKids.getArriveUrlPicture() != null) {
                    dataKidResponse.setPicture(attendanceArriveKids.getArriveUrlPicture());
                } else {
                    dataKidResponse.setPicture("");
                }

                dataKidResponse.setAvatar(ConvertData.getAvatarKid(attendanceKidsSaved.getKids()));

                dataKidResponse.setStatus(attendanceKidsSaved.isAttendanceArrive());

                if (attendanceKidsSaved.getKids().getFullName() != null) {
                    dataKidResponse.setKidName(attendanceKidsSaved.getKids().getFullName());
                } else {
                    dataKidResponse.setKidName("");
                }

                if (attendanceArriveKids.getArriveContent() != null) {
                    dataKidResponse.setContent(attendanceArriveKids.getArriveContent());
                } else {
                    dataKidResponse.setContent("");
                }
                dataKidResponse.setId(id);
                dataList.add(dataKidResponse);
            }
        }

        return dataList;


    }

//    private void sendAppSend(UserPrincipal principal, Long idTitle, List<AttendanceKids> attendanceKids, String typeSend) {
//        if (principal.getSchoolConfig().isApprovedAttendanceArrive()) {
//            LocalDateTime localDateTime = LocalDateTime.now();
//            AppSend appSend = new AppSend();
//            Optional<WebSystemTitle> webSystemTitle = webSystemTitleService.findById(idTitle);
//            String titleAppTeacher = webSystemTitle.get().getTitle();
//            String contentAppTeacher = webSystemTitle.get().getContent();
//            appSend.setReceivedNumber(attendanceKids.size());
//            appSend.setIdSchool(principal.getIdSchoolLogin());
//            appSend.setApproved(principal.getSchoolConfig().isAppSendApproved());
//            appSend.setAppType(principal.getAppType());
//            appSend.setSendContent(contentAppTeacher);
//            appSend.setCreatedBy(principal.getFullName());
//            appSend.setSendTitle(titleAppTeacher);
//            appSend.setSendType(typeSend);
//            appSend.setTimeSend(localDateTime);
//            appSendRepository.save(appSend);
//            attendanceKids.forEach(arriveKid -> {
//                Long kid = arriveKid.getKids().getId();
//                Optional<Kids> kids = kidsRepository.findById(kid);
//                if (kids.isPresent() && kids.get().getParent() != null) {
//                    Receivers receivers = new Receivers();
//                    receivers.setIdClass(kids.get().getMaClass().getId());
//                    receivers.setIdKids(kids.get().getId());
//                    receivers.setApproved(principal.getSchoolConfig().isAppSendApproved());
//                    receivers.setIdUserReceiver(kids.get().getParent().getMaUser().getId());
//                    receivers.setIdSchool(kids.get().getIdSchool());
//                    receivers.setCreatedBy(principal.getFullName());
//                    receivers.setAppSend(appSend);
//                    receiversRepository.save(receivers);
//                }
//            });
//        }
//    }

    @Override
    public AttendanceDayTeacherResponse searchAttendanceKidsLeave(UserPrincipal principal, LocalDate localDate) {

        AttendanceDayTeacherResponse attendanceDayTeacherResponse = new AttendanceDayTeacherResponse();
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidDay(principal, localDate);
        if (attendanceKids.size() > 0) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), localDate);
            List<AttendanceKids> attendance = new ArrayList<>();
            List<AttendanceKids> attendanceOff = new ArrayList<>();
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return attendanceDayTeacherResponse;
            } else {
                attendanceKids.forEach(x -> {
                    if (x.isAttendanceLeave()) {
                        attendance.add(x);
                    }
                });
                attendanceDayTeacherResponse.setSumKid(attendanceKids.size());
                attendanceDayTeacherResponse.setSumKidAttendance(attendance.size());
                attendanceDayTeacherResponse.setSumKidNoAttendance(attendanceKids.size() - attendance.size());

                attendanceKids.forEach(day -> {
                    if (day.isAttendanceArrive()) {
                        if (!(day.getAttendanceArriveKids().isMorning()) && !(day.getAttendanceArriveKids().isAfternoon()) && !(day.getAttendanceArriveKids().isEvening())) {
                            attendanceOff.add(day);
                        }
                    }
                });
                attendanceDayTeacherResponse.setSumKidOff(attendanceOff.size());
                return attendanceDayTeacherResponse;
            }
        } else {
            return attendanceDayTeacherResponse;
        }
    }

    @Override
    public List<AttendanceKidsLeaveTeacherResponse> searchAttendanceKidsDetailLeave(UserPrincipal principal, LocalDate localDate) {

        List<AttendanceKidsLeaveTeacherResponse> dataList = new ArrayList<>();
        String checkNull = "";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(localDate, principal.getIdClassLogin());
        if (CollectionUtils.isEmpty(kidsList)) {
            return dataList;
        }
        List<Long> idKidList = kidsList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidsDateList(idKidList, localDate);
        if (attendanceKids.size() > 0) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), localDate);
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataList;
            } else {
                attendanceKids.forEach(attendance -> {
                    AttendanceKidsLeaveTeacherResponse data = new AttendanceKidsLeaveTeacherResponse();
                    data.setId(attendance.getAttendanceLeaveKids().getId());
                    data.setIdKid(attendance.getKids().getId());
                    data.setKidName(attendance.getKids().getFullName());
                    data.setNickName(StringUtils.isNotBlank(attendance.getKids().getNickName()) ? attendance.getKids().getNickName() : "");
                    data.setStatusLeave(attendance.isAttendanceLeave());

                    data.setAvatar(ConvertData.getAvatarKid(attendance.getKids()));
                    if (attendance.getAttendanceLeaveKids().getLeaveUrlPicture() != null) {
                        data.setPicture(attendance.getAttendanceLeaveKids().getLeaveUrlPicture());
                    } else {
                        data.setPicture(checkNull);
                    }
                    if (attendance.getAttendanceLeaveKids().getLeaveContent() != null) {
                        data.setContent(attendance.getAttendanceLeaveKids().getLeaveContent());
                    } else {
                        data.setContent(checkNull);
                    }
                    if (attendance.getAttendanceLeaveKids().getTimeLeaveKid() != null) {
                        data.setTime(attendance.getAttendanceLeaveKids().getTimeLeaveKid().format(df));
                    } else {
                        data.setTime(checkNull);
                    }
                    if (attendance.getAttendanceArriveKids().isMorning() || attendance.getAttendanceArriveKids().isAfternoon() || attendance.getAttendanceArriveKids().isEvening()) {
                        data.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                    } else data.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);

                    if (attendance.isAttendanceArrive()) {
                        if (attendance.getAttendanceArriveKids().isMorning() || attendance.getAttendanceArriveKids().isAfternoon() || attendance.getAttendanceArriveKids().isEvening()) {
                            data.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                        } else data.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                    } else {
                        data.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                    }

                    dataList.add(data);
                });
            }
            return dataList;
        } else {
            return dataList;
        }
    }

    @Transactional
    @Override
    public AiAttendanceKidsLeaveTeacherResponse createAiAttendanceKidsTeacherLeave(UserPrincipal principal, AiAttendanceKidLeaveTeacherRequest aiLeaverRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        Long idKid = aiLeaverRequest.getId();
        Long idClass = principal.getIdClassLogin();
        LocalDate date = LocalDate.now();
        AiAttendanceKidsLeaveTeacherResponse dataResult = new AiAttendanceKidsLeaveTeacherResponse();
        AttendanceKids attendanceKids = attendanceKidsRepository.findByDelActiveTrueAndAttendanceDateAndKids_IdAndMaClass_Id(date, idKid, idClass);
        if (attendanceKids == null) {
            dataResult.setStatus(AppConstant.APP_FALSE);
            return dataResult;
        }
        AttendanceLeaveKids attendanceLeaveKids = attendanceKids.getAttendanceLeaveKids();
        AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.getAttendanceConfig(), date);
        if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
            dataResult.setStatus(AppConstant.APP_FALSE);
            return dataResult;
        }
        attendanceLeaveKids.setLeaveContent(aiLeaverRequest.getContent());
        attendanceLeaveKids.setStatusLeave(AppConstant.APP_TRUE);

        if (aiLeaverRequest.getPicture() != null) {
            HandleFileResponse handleFileResponse = null;
            try {
                handleFileResponse = HandleFileUtils.getUrlPictureSaved(aiLeaverRequest.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String urlWeb = handleFileResponse.getUrlWeb();
            String urlLocal = handleFileResponse.getUrlLocal();
            attendanceLeaveKids.setLeaveUrlPicture(urlWeb);
            attendanceLeaveKids.setLeaveUrlPictureLocal(urlLocal);
        }

        LocalTime time = LocalTime.parse(ConvertData.convertTimeHHMM(LocalTime.now()));
        attendanceLeaveKids.setTimeLeaveKid(time);

        long timeCv = ConvertData.convertLocalTimeToLong(time);
        long timeCf = ConvertData.convertLocalTimeToLong(principal.getSchoolConfig().getTimePickupKid());
        if (timeCv > timeCf) {
            attendanceLeaveKids.setMinutePickupLate((int) (timeCv - timeCf));
        } else {
            attendanceLeaveKids.setMinutePickupLate(0);
        }

        AttendanceLeaveKids attendanceLeaveKidsSave = attendanceLeaveKidsRepository.save(attendanceLeaveKids);

        attendanceKids.setAttendanceLeave(AppConstant.APP_TRUE);
        attendanceKids.setAttendanceLeaveKids(attendanceLeaveKidsSave);
        boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_LEAVE, principal, attendanceKids);
        AttendanceKids attendanceKidsSave = attendanceKidsRepository.save(attendanceKids);
        if (checkSendFirebase) {
            long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidsSave, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.TEACHER);
            if (idWebSystem != 0) {
                //send firebase
                firebaseFunctionService.sendParentByTeacherNoContent(idWebSystem, attendanceKidsSave.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE, "");
            }
        }

        dataResult.setStatus(AppConstant.APP_TRUE);
        return dataResult;
    }

    @Override
    public StatusAttendanceDay checkAttendanceStatusDay(UserPrincipal principal) {
        CommonValidate.checkDataTeacher(principal);
        StatusAttendanceDay response = new StatusAttendanceDay();
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidDay(principal, LocalDate.now());
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

    @Transactional
    @Override
    public List<AttendanceKidsLeaveTeacherResponse> createAttendanceKidsTeacherLeave(UserPrincipal principal, AttendanceKidLeaveTeacherRequest attendanceKidLeaveTeacherRequest) throws FirebaseMessagingException, IOException {
        CommonValidate.checkDataTeacher(principal);
        Long idLeave = attendanceKidLeaveTeacherRequest.getIdLeave().get(0);
        AttendanceLeaveKids attendanceLeaveKidsOne = attendanceLeaveKidsRepository.findByIdAndDelActiveTrue(idLeave);
        LocalDate date = attendanceLeaveKidsOne.getAttendanceKids().getAttendanceDate();
        // ngày điểm danh phải lớn hơn ngày config
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendace(date, principal.getSchoolConfig().getAgainAttendance());
        }
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidDay(principal, date);
        List<AttendanceKidsLeaveTeacherResponse> dataResponse = new ArrayList<>();
        List<AttendanceKids> appsendLeave = new ArrayList<>();
        if (attendanceKids.size() > 0) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), date);
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataResponse;
            } else {
                for (Long leave : attendanceKidLeaveTeacherRequest.getIdLeave()) {
                    AttendanceKidsLeaveTeacherResponse dataKidResponse = new AttendanceKidsLeaveTeacherResponse();
                    AttendanceLeaveKids attendanceLeaveKidNew = attendanceLeaveKidsRepository.findByIdAndDelActiveTrue(leave);
                    boolean checkFireBase = attendanceLeaveKidNew.getIdCreated() == null;
                    AttendanceKids attendanceKidNew = attendanceLeaveKidNew.getAttendanceKids();
                    attendanceLeaveKidNew.setLeaveContent(attendanceKidLeaveTeacherRequest.getContent());
                    attendanceLeaveKidNew.setStatusLeave(AppConstant.APP_TRUE);
                    if (attendanceKidLeaveTeacherRequest.getPicture() != null) {
                        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(attendanceKidLeaveTeacherRequest.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                        String urlWeb = handleFileResponse.getUrlWeb();
                        String urlLocal = handleFileResponse.getUrlLocal();
                        attendanceLeaveKidNew.setLeaveUrlPicture(urlWeb);
                        attendanceLeaveKidNew.setLeaveUrlPictureLocal(urlLocal);
                    } else {
                        if (attendanceKidLeaveTeacherRequest.isDeletePicture()) {
                            attendanceLeaveKidNew.setLeaveUrlPicture(null);
                        }
                    }

                    if (!Strings.isEmpty(attendanceKidLeaveTeacherRequest.getTime())) {
                        LocalTime time = LocalTime.parse(attendanceKidLeaveTeacherRequest.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
                        attendanceLeaveKidNew.setTimeLeaveKid(time);
                        long timeCv = ConvertData.convertLocalTimeToLong(time);
                        long timeCf = ConvertData.convertLocalTimeToLong(principal.getSchoolConfig().getTimePickupKid());
                        if (timeCv > timeCf) {
                            attendanceLeaveKidNew.setMinutePickupLate((int) (timeCv - timeCf));
                        } else {
                            attendanceLeaveKidNew.setMinutePickupLate(0);
                        }

                    } else {
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                        String timeNew = df.format(LocalTime.now());
                        LocalTime time = LocalTime.parse(timeNew);

                        if (attendanceLeaveKidNew.getTimeLeaveKid() == null) {
                            attendanceLeaveKidNew.setTimeLeaveKid(time);

                            long timeCv = ConvertData.convertLocalTimeToLong(time);
                            long timeCf = ConvertData.convertLocalTimeToLong(principal.getSchoolConfig().getTimePickupKid());
                            if (timeCv > timeCf) {
                                attendanceLeaveKidNew.setMinutePickupLate((int) (timeCv - timeCf));
                            } else {
                                attendanceLeaveKidNew.setMinutePickupLate(0);
                            }
                        }
                    }

                    AttendanceLeaveKids attendanceLeaveKids = attendanceLeaveKidsRepository.save(attendanceLeaveKidNew);
                    boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_LEAVE, principal, attendanceKidNew);
                    attendanceKidNew.setAttendanceLeave(AppConstant.APP_TRUE);
                    attendanceKidNew.setAttendanceLeaveKids(attendanceLeaveKids);
                    AttendanceKids attendanceKids1 = attendanceKidsRepository.save(attendanceKidNew);
                    if (checkSendFirebase) {
                        long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKidNew, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.TEACHER);
                        if (idWebSystem != 0) {
                            //send firebase
                            firebaseFunctionService.sendParentByTeacherNoContent(idWebSystem, attendanceKidNew.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE, "");
                        }
                    }
                    //du liệu trả về
                    if (attendanceLeaveKids.getTimeLeaveKid() != null) {
                        dataKidResponse.setTime(attendanceLeaveKids.getTimeLeaveKid().toString());
                    } else {
                        dataKidResponse.setTime("");
                    }
                    if (attendanceLeaveKids.getLeaveUrlPicture() != null) {
                        dataKidResponse.setPicture(attendanceLeaveKids.getLeaveUrlPicture());
                    } else {
                        dataKidResponse.setPicture("");
                    }
                    dataKidResponse.setAvatar(ConvertData.getAvatarKid(attendanceKidNew.getKids()));

                    if (attendanceKids1.isAttendanceArrive()) {
                        if (attendanceKids1.getAttendanceArriveKids().isMorning() || attendanceKids1.getAttendanceArriveKids().isAfternoon() || attendanceKids1.getAttendanceArriveKids().isEvening()) {
                            dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                        } else dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                    } else {
                        dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                    }
                    dataKidResponse.setStatusLeave(attendanceKidNew.isAttendanceLeave());
                    dataKidResponse.setKidName(attendanceKidNew.getKids().getFullName());
                    dataKidResponse.setContent(attendanceLeaveKids.getLeaveContent());
                    dataKidResponse.setId(leave);
                    dataResponse.add(dataKidResponse);
                }
                return dataResponse;
            }
        }
        return dataResponse;

    }

    @Transactional
    @Override
    public List<AttendanceKidsLeaveTeacherResponse> createAttendanceKidsMultiTeacherLeave(UserPrincipal principal, AttendanceKidLeaveTeacherMultiRequest leaveTeacherMultiRequest) throws FirebaseMessagingException, IOException {
        CommonValidate.checkDataTeacher(principal);
        Long idLeave = leaveTeacherMultiRequest.getAttendanceOneKidLeaveList().get(0).getId();

        AttendanceLeaveKids attendanceLeaveKidsOne = attendanceLeaveKidsRepository.findByIdAndDelActiveTrue(idLeave);
        LocalDate date = attendanceLeaveKidsOne.getAttendanceKids().getAttendanceDate();

        List<AttendanceKidsLeaveTeacherResponse> dataResponse = new ArrayList<>();
        List<AttendanceKids> appsendLeave = new ArrayList<>();

        if (attendanceLeaveKidsOne != null) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceLeaveKidsOne.getAttendanceKids().getAttendanceConfig(), date);
            // ngày điểm danh phải lớn hơn ngày config
            if (principal.getSchoolConfig().getAgainAttendance() != null) {
                this.checkAgainAttendace(date, principal.getSchoolConfig().getAgainAttendance());
            }
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataResponse;
            }

            for (AttendanceOneKidLeaveTeacherRequest leave : leaveTeacherMultiRequest.getAttendanceOneKidLeaveList()) {
                AttendanceKidsLeaveTeacherResponse dataKidResponse = new AttendanceKidsLeaveTeacherResponse();

                AttendanceLeaveKids attendanceLeaveKidNew = attendanceLeaveKidsRepository.findByIdAndDelActiveTrue(leave.getId());
                boolean checkFireBase = attendanceLeaveKidNew.getIdCreated() == null;
                AttendanceKids attendanceKidNew = attendanceLeaveKidNew.getAttendanceKids();

                attendanceLeaveKidNew.setLeaveContent(leave.getContent());
                attendanceLeaveKidNew.setStatusLeave(AppConstant.APP_TRUE);

                if (leave.getPicture() != null) {
                    HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(leave.getPicture(), principal.getIdSchoolLogin(), UploadDownloadConstant.DIEM_DANH);
                    String urlWeb = handleFileResponse.getUrlWeb();
                    String urlLocal = handleFileResponse.getUrlLocal();
                    attendanceLeaveKidNew.setLeaveUrlPicture(urlWeb);
                    attendanceLeaveKidNew.setLeaveUrlPictureLocal(urlLocal);
                } else {
                    if (leave.isDeletePicture()) {
                        attendanceLeaveKidNew.setLeaveUrlPicture(null);
                    }
                }

                if (!Strings.isEmpty(leave.getTime())) {
                    LocalTime time = LocalTime.parse(leave.getTime(), DateTimeFormatter.ofPattern("HH:mm"));
                    attendanceLeaveKidNew.setTimeLeaveKid(time);
                    long timeCv = ConvertData.convertLocalTimeToLong(time);
                    long timeCf = ConvertData.convertLocalTimeToLong(principal.getSchoolConfig().getTimePickupKid());
                    if (timeCv > timeCf) {
                        attendanceLeaveKidNew.setMinutePickupLate((int) (timeCv - timeCf));
                    } else {
                        attendanceLeaveKidNew.setMinutePickupLate(0);
                    }

                } else {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
                    String timeNew = df.format(LocalTime.now());
                    LocalTime time = LocalTime.parse(timeNew);

                    if (attendanceLeaveKidNew.getTimeLeaveKid() == null) {
                        attendanceLeaveKidNew.setTimeLeaveKid(time);

                        long timeCv = ConvertData.convertLocalTimeToLong(time);
                        long timeCf = ConvertData.convertLocalTimeToLong(principal.getSchoolConfig().getTimePickupKid());
                        if (timeCv > timeCf) {
                            attendanceLeaveKidNew.setMinutePickupLate((int) (timeCv - timeCf));
                        } else {
                            attendanceLeaveKidNew.setMinutePickupLate(0);
                        }
                    }
                }

                AttendanceLeaveKids attendanceLeaveKids = attendanceLeaveKidsRepository.save(attendanceLeaveKidNew);
                boolean checkSendFirebase = AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_LEAVE, principal, attendanceKidNew);
                attendanceKidNew.setAttendanceLeave(AppConstant.APP_TRUE);
                attendanceKidNew.setAttendanceLeaveKids(attendanceLeaveKids);
                AttendanceKids attendanceKids1 = attendanceKidsRepository.save(attendanceKidNew);
                if (checkSendFirebase) {
                    long idWebSystem = AttendanceKidsUtil.sendFirebaseConditions(principal, attendanceKids1, AttendanceConstant.ATTENDANCE_LEAVE, AppTypeConstant.TEACHER);
                    if (idWebSystem != 0) {
                        //send firebase
                        firebaseFunctionService.sendParentByTeacherNoContent(idWebSystem, attendanceKids1.getKids(), FirebaseConstant.CATEGORY_ATTENDANCE, "");
                    }
                }

                //du liệu trả về
                if (attendanceLeaveKids.getTimeLeaveKid() != null) {
                    dataKidResponse.setTime(attendanceLeaveKids.getTimeLeaveKid().toString());
                } else {
                    dataKidResponse.setTime("");
                }
                if (attendanceLeaveKids.getLeaveUrlPicture() != null) {
                    dataKidResponse.setPicture(attendanceLeaveKids.getLeaveUrlPicture());
                } else {
                    dataKidResponse.setPicture("");
                }
                dataKidResponse.setAvatar(ConvertData.getAvatarKid(attendanceKidNew.getKids()));

                if (attendanceKids1.isAttendanceArrive()) {
                    if (attendanceKids1.getAttendanceArriveKids().isMorning() || attendanceKids1.getAttendanceArriveKids().isAfternoon() || attendanceKids1.getAttendanceArriveKids().isEvening()) {
                        dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                    } else dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                } else {
                    dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                }
                dataKidResponse.setStatusLeave(attendanceKidNew.isAttendanceLeave());
                dataKidResponse.setKidName(attendanceKidNew.getKids().getFullName());
                dataKidResponse.setContent(attendanceLeaveKids.getLeaveContent());
                dataKidResponse.setId(leave.getId());
                dataResponse.add(dataKidResponse);
            }
        }
        return dataResponse;
    }

    @Override
    public AttendanceDayTeacherResponse searchAttendanceKidsEat(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        AttendanceDayTeacherResponse attendanceDayTeacherResponse = new AttendanceDayTeacherResponse();
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidDay(principal, localDate);
        if (!CollectionUtils.isEmpty(attendanceKids)) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), localDate);
            List<AttendanceKids> attendance = new ArrayList<>();
            List<AttendanceKids> attendanceOff = new ArrayList<>();
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return attendanceDayTeacherResponse;
            }
            attendanceKids.forEach(x -> {
                if (x.isAttendanceEat()) {
                    attendance.add(x);
                }
            });

            attendanceDayTeacherResponse.setSumKid(attendanceKids.size());
            attendanceDayTeacherResponse.setSumKidAttendance(attendance.size());
            attendanceDayTeacherResponse.setSumKidNoAttendance(attendanceKids.size() - attendance.size());

            attendanceKids.forEach(day -> {
                if (day.isAttendanceArrive()) {
                    if (!(day.getAttendanceArriveKids().isMorning()) && !(day.getAttendanceArriveKids().isAfternoon()) && !(day.getAttendanceArriveKids().isEvening())) {
                        attendanceOff.add(day);
                    }
                }
            });
            attendanceDayTeacherResponse.setSumKidOff(attendanceOff.size());
        }
        return attendanceDayTeacherResponse;

    }


    @Override
    public List<AttendanceKidEatTeacherResponse> searchAttendanceKidsDetailEat(UserPrincipal principal, LocalDate
            localDate) {

        List<AttendanceKidEatTeacherResponse> dataList = new ArrayList<>();
        List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(localDate, principal.getIdClassLogin());
        if (CollectionUtils.isEmpty(kidsList)) {
            return dataList;
        }
        List<Long> idKidList = kidsList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidsDateList(idKidList, localDate);
        String checkNull = "";
        if (attendanceKids.size() > 0) {

            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), localDate);

            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataList;
            } else {
                attendanceKids.forEach(attendance -> {
                    AttendanceEatKids attendanceEatKids = attendance.getAttendanceEatKids();
                    AttendanceKidEatTeacherResponse data = new AttendanceKidEatTeacherResponse();
                    data.setId(attendanceEatKids.getId());
                    data.setKidName(attendance.getKids().getFullName());
                    data.setNickName(StringUtils.isNotBlank(attendance.getKids().getNickName()) ? attendance.getKids().getNickName() : "");
                    data.setStatusEat(AttendanceKidsUtil.checkEat(attendanceEatKids));

                    if (attendance.isAttendanceArrive()) {
                        if (attendance.getAttendanceArriveKids().isMorning() || attendance.getAttendanceArriveKids().isAfternoon() || attendance.getAttendanceArriveKids().isEvening()) {
                            data.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                        } else data.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                    } else {
                        data.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                    }

                    data.setAvatar(ConvertData.getAvatarKid(attendance.getKids()));
                    if (attendanceConfigResponse.isMorningAttendanceArrive()) {
                        if (attendanceConfigResponse.isMorningEat()) {
                            data.setMorning(attendanceEatKids.isBreakfast());
                        }
                        if (attendanceConfigResponse.isSecondMorningEat()) {
                            data.setSecondMorning(attendanceEatKids.isSecondBreakfast());
                        }
                    }
                    if (attendanceConfigResponse.isMorningAttendanceArrive() || attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                        if (attendanceConfigResponse.isLunchEat()) {
                            data.setLunch(attendanceEatKids.isLunch());
                        }
                    }
                    if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                        if (attendanceConfigResponse.isAfternoonEat()) {
                            data.setAfternoon(attendanceEatKids.isAfternoon());
                        }
                        if (attendanceConfigResponse.isSecondAfternoonEat()) {
                            data.setSecondAfternoon(attendanceEatKids.isSecondAfternoon());
                        }
                    }
                    if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                        if (attendanceConfigResponse.isEveningEat()) {
                            data.setDinner(attendanceEatKids.isDinner());
                        }
                    }
                    dataList.add(data);
                });
            }
            return dataList;
        } else {
            return dataList;
        }
    }

    @Override
    public List<AttendanceKidEatTeacherResponse> createAttendanceKidsTeacherEat(UserPrincipal
                                                                                        principal, AttendanceKidEatTeacherRequest attendanceKidEatTeacherRequest) {

        Long idEat = attendanceKidEatTeacherRequest.getIdEat().get(0);

        AttendanceEatKids attendanceEatKidsOne = attendanceEatKidsRepository.findByIdAndDelActiveTrue(idEat);

        LocalDate date = attendanceEatKidsOne.getAttendanceKids().getAttendanceDate();
        // ngày điểm danh phải lớn hơn ngày config
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendace(date, principal.getSchoolConfig().getAgainAttendance());
        }

        List<AttendanceKids> attendanceKids = attendanceKidsRepository.findAttendanceKidDay(principal, date);


        List<AttendanceKidEatTeacherResponse> dataResponse = new ArrayList<>();

        if (attendanceKids.size() > 0) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceKids.get(0).getAttendanceConfig(), date);
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataResponse;
            }
            attendanceKidEatTeacherRequest.getIdEat().forEach(eat -> {

                AttendanceKidEatTeacherResponse dataKidResponse = new AttendanceKidEatTeacherResponse();

                AttendanceEatKids attendanceEatKids = attendanceEatKidsRepository.findByIdAndDelActiveTrue(eat);

                AttendanceKids attendanceKidNew = attendanceEatKids.getAttendanceKids();
                attendanceKidNew.setAttendanceEat(AppConstant.APP_TRUE);
                if (!attendanceKidEatTeacherRequest.isMorning() && !attendanceKidEatTeacherRequest.isSecondMorning() && !attendanceKidEatTeacherRequest.isLunch() && !attendanceKidEatTeacherRequest.isAfternoon() && !attendanceKidEatTeacherRequest.isSecondAfternoon() && !attendanceKidEatTeacherRequest.isDinner()) {
                    attendanceKidNew.setAttendanceEat(AppConstant.APP_FALSE);
                }
                if (attendanceConfigResponse.isMorningAttendanceArrive()) {
                    if (attendanceConfigResponse.isMorningEat()) {
                        attendanceEatKids.setBreakfast(attendanceKidEatTeacherRequest.isMorning());
                    }
                    if (attendanceConfigResponse.isSecondMorningEat()) {
                        attendanceEatKids.setSecondBreakfast(attendanceKidEatTeacherRequest.isSecondMorning());
                    }
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isMorningAttendanceArrive()) {
                    if (attendanceConfigResponse.isLunchEat()) {
                        attendanceEatKids.setLunch(attendanceKidEatTeacherRequest.isLunch());
                    }
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                    if (attendanceConfigResponse.isAfternoonEat()) {
                        attendanceEatKids.setAfternoon(attendanceKidEatTeacherRequest.isAfternoon());
                    }
                    if (attendanceConfigResponse.isSecondAfternoonEat()) {
                        attendanceEatKids.setSecondAfternoon(attendanceKidEatTeacherRequest.isSecondAfternoon());
                    }
                }
                if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                    if (attendanceConfigResponse.isEveningEat()) {
                        attendanceEatKids.setDinner(attendanceKidEatTeacherRequest.isDinner());
                    }
                }
                AttendanceEatKids attendanceEatKid = attendanceEatKidsRepository.save(attendanceEatKids);
                attendanceKidNew.setAttendanceEatKids(attendanceEatKids);
                AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_EAT, principal, attendanceKidNew);
                AttendanceKids attendanceKids1 = attendanceKidsRepository.save(attendanceKidNew);


                //du liệu trả về
                dataKidResponse.setAvatar(ConvertData.getAvatarKid(attendanceKidNew.getKids()));
                if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                    if (attendanceConfigResponse.isMorningEat()) {
                        dataKidResponse.setMorning(attendanceEatKid.isBreakfast());
                    }
                    if (attendanceConfigResponse.isSecondMorningEat()) {
                        dataKidResponse.setSecondMorning(attendanceEatKid.isSecondBreakfast());
                    }
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isMorningAttendanceArrive()) {
                    if (attendanceConfigResponse.isLunchEat()) {
                        dataKidResponse.setLunch(attendanceEatKid.isLunch());
                    }
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                    if (attendanceConfigResponse.isAfternoonEat()) {
                        dataKidResponse.setAfternoon(attendanceEatKid.isAfternoon());
                    }
                    if (attendanceConfigResponse.isSecondAfternoonEat()) {
                        dataKidResponse.setSecondAfternoon(attendanceEatKid.isSecondAfternoon());
                    }
                }
                if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                    if (attendanceConfigResponse.isEveningEat()) {
                        dataKidResponse.setDinner(attendanceEatKid.isDinner());
                    }
                }

                if (attendanceKids1.isAttendanceArrive()) {
                    if (attendanceKids1.getAttendanceArriveKids().isMorning() || attendanceKids1.getAttendanceArriveKids().isAfternoon() || attendanceKids1.getAttendanceArriveKids().isEvening()) {
                        dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                    } else dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                } else {
                    dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                }
                dataKidResponse.setAvatar(ConvertData.getAvatarKid(attendanceKidNew.getKids()));
                dataKidResponse.setStatusEat(AttendanceKidsUtil.checkEat(attendanceEatKid));
                dataKidResponse.setKidName(attendanceKidNew.getKids().getFullName());
                dataKidResponse.setId(eat);
                dataResponse.add(dataKidResponse);

            });
        }
        return dataResponse;

    }

    @Override
    public List<AttendanceKidEatTeacherResponse> createAttendanceKidsMultiTeacherEat(UserPrincipal principal, AttendanceKidEatTeacherMultiRequest attendanceKidEatTeacherMultiRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idEat = attendanceKidEatTeacherMultiRequest.getAttendanceOneKidEatList().get(0).getId();
        AttendanceEatKids attendanceEatKidsOne = attendanceEatKidsRepository.findByIdAndDelActiveTrue(idEat);
        LocalDate date = attendanceEatKidsOne.getAttendanceKids().getAttendanceDate();
        // ngày điểm danh phải lớn hơn ngày config
        if (principal.getSchoolConfig().getAgainAttendance() != null) {
            this.checkAgainAttendace(date, principal.getSchoolConfig().getAgainAttendance());
        }

        List<AttendanceKidEatTeacherResponse> dataResponse = new ArrayList<>();

        if (attendanceEatKidsOne != null) {
            AttendanceConfigResponse attendanceConfigResponse = ConvertData.convertAttendanceConfig(attendanceEatKidsOne.getAttendanceKids().getAttendanceConfig(), date);
            if (!(attendanceConfigResponse.isAfternoonAttendanceArrive()) && !(attendanceConfigResponse.isEveningAttendanceArrive()) && !(attendanceConfigResponse.isMorningAttendanceArrive())) {
                return dataResponse;
            }
            attendanceKidEatTeacherMultiRequest.getAttendanceOneKidEatList().forEach(eat -> {

                AttendanceKidEatTeacherResponse dataKidResponse = new AttendanceKidEatTeacherResponse();

                AttendanceEatKids attendanceEatKids = attendanceEatKidsRepository.findByIdAndDelActiveTrue(eat.getId());

                AttendanceKids attendanceKidNew = attendanceEatKids.getAttendanceKids();

                attendanceKidNew.setAttendanceEat(AppConstant.APP_TRUE);

                if (attendanceConfigResponse.isMorningAttendanceArrive()) {
                    if (attendanceConfigResponse.isMorningEat()) {
                        attendanceEatKids.setBreakfast(eat.isMorning());
                    }
                    if (attendanceConfigResponse.isSecondMorningEat()) {
                        attendanceEatKids.setSecondBreakfast(eat.isSecondMorning());
                    }
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isMorningAttendanceArrive()) {
                    if (attendanceConfigResponse.isLunchEat()) {
                        attendanceEatKids.setLunch(eat.isLunch());
                    }
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                    if (attendanceConfigResponse.isAfternoonEat()) {
                        attendanceEatKids.setAfternoon(eat.isAfternoon());
                    }
                    if (attendanceConfigResponse.isSecondAfternoonEat()) {
                        attendanceEatKids.setSecondAfternoon(eat.isSecondAfternoon());

                    }
                }
                if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                    if (attendanceConfigResponse.isEveningEat()) {
                        attendanceEatKids.setDinner(eat.isDinner());
                    }
                }
                AttendanceEatKids attendanceEatKid = attendanceEatKidsRepository.save(attendanceEatKids);
                attendanceKidNew.setAttendanceEatKids(attendanceEatKids);
                AttendanceKidsUtil.setAuditingAttendance(AttendanceConstant.ATTENDANCE_EAT, principal, attendanceKidNew);
                AttendanceKids attendanceKids1 = attendanceKidsRepository.save(attendanceKidNew);


                //du liệu trả về
                dataKidResponse.setAvatar(ConvertData.getAvatarKid(attendanceKidNew.getKids()));
                if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                    if (attendanceConfigResponse.isMorningEat()) {
                        dataKidResponse.setMorning(attendanceEatKid.isBreakfast());
                    }
                    if (attendanceConfigResponse.isSecondMorningEat()) {
                        dataKidResponse.setSecondMorning(attendanceEatKid.isSecondBreakfast());
                    }
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isMorningAttendanceArrive()) {
                    if (attendanceConfigResponse.isLunchEat()) {
                        dataKidResponse.setLunch(attendanceEatKid.isLunch());
                    }
                }
                if (attendanceConfigResponse.isAfternoonAttendanceArrive()) {
                    if (attendanceConfigResponse.isAfternoonEat()) {
                        dataKidResponse.setAfternoon(attendanceEatKid.isAfternoon());
                    }
                    if (attendanceConfigResponse.isSecondAfternoonEat()) {
                        dataKidResponse.setSecondAfternoon(attendanceEatKid.isSecondAfternoon());
                    }
                }
                if (attendanceConfigResponse.isEveningAttendanceArrive()) {
                    if (attendanceConfigResponse.isEveningEat()) {
                        dataKidResponse.setDinner(attendanceEatKid.isDinner());
                    }
                }

                if (attendanceKids1.isAttendanceArrive()) {
                    if (attendanceKids1.getAttendanceArriveKids().isMorning() || attendanceKids1.getAttendanceArriveKids().isAfternoon() || attendanceKids1.getAttendanceArriveKids().isEvening()) {
                        dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_ARRIVE);
                    } else dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_OFF);
                } else {
                    dataKidResponse.setStatusArrive(AttendanceConstant.ATTENDANCE_NO_ARRIVE);
                }
                dataKidResponse.setAvatar(ConvertData.getAvatarKid(attendanceKidNew.getKids()));
                dataKidResponse.setStatusEat(AttendanceKidsUtil.checkEat(attendanceEatKid));
                dataKidResponse.setKidName(attendanceKidNew.getKids().getFullName());
                dataKidResponse.setId(eat.getId());
                dataResponse.add(dataKidResponse);
            });
        }
        return dataResponse;
    }

    @Override
    public List<String> searchAttendanceKidsArriveSample(UserPrincipal principal) {
        return this.checkAttendanceSample(principal, AttendanceConstant.ATTENDANCE_TYPE_ARRIVE);
    }

    @Override
    public List<String> searchAttendanceKidsLeaveSample(UserPrincipal principal) {
        return this.checkAttendanceSample(principal, AttendanceConstant.ATTENDANCE_TYPE_LEAVE);
    }

    private List<String> checkAttendanceSample(UserPrincipal principal, String type) {
        List<AttendanceSample> attendanceSampleList;
        if (principal.getSchoolConfig().isShowAttentendanceSys()) {
            attendanceSampleList = attendanceSampleRepository.findAllAttendanceSampleWithType(principal.getIdSchoolLogin(), SystemConstant.ID_SYSTEM, type);
        } else {
            attendanceSampleList = attendanceSampleRepository.findAllAttendanceSampleWithType(principal.getIdSchoolLogin(), null, type);
        }
        return attendanceSampleList.stream().map(AttendanceSample::getAttendanceContent).collect(Collectors.toList());
    }

    private void checkAgainAttendace(LocalDate date, int day) {
        // ngày điểm danh phải lớn hơn ngày config
        LocalDate dateCheck = LocalDate.now().minusDays(day);
        if (date.isBefore(dateCheck)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không được điểm danh muộn quá " + day + " ngày");
        }
    }


}
