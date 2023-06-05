package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.entity.school.ConfigNotifyPlus;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.enums.StudentStatusEnum;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.request.ContentAndIdMobileRequest;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.MobileFileTeacher;
import com.example.onekids_project.mobile.response.ReplyTypeEditObject;
import com.example.onekids_project.mobile.response.StartEndDateObject;
import com.example.onekids_project.mobile.teacher.request.evaluate.*;
import com.example.onekids_project.mobile.teacher.response.evaluate.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.EvaluateTeacherService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.attendancekids.AttendanceConfigResponse;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import com.example.onekids_project.validate.RequestValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class EvaluateTeacherServiceImpl implements EvaluateTeacherService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private EvaluateDateRepository evaluateDateRepository;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private EvaluateAttachFileRepository evaluateAttachFileRepository;

    @Autowired
    private EvaluateWeekFileRepository evaluateWeekFileRepository;

    @Autowired
    private EvaluateMonthFileRepository evaluateMonthFileRepository;

    @Autowired
    private EvaluatePeriodicFileRepository evaluatePeriodicFileRepository;

    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;

    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;

    @Autowired
    private EvaluatePeriodicRepository evaluatePeriodicRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private KidsClassDateRepository kidsClassDateRepository;

    @Autowired
    private WebSystemTitleService webSystemTitleService;

    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private ConfigNotifyPlusRepository configNotifyPlusRepository;


    @Override
    public TotalTeacherResponse findSchoolUnread(UserPrincipal principal, Long idKid, LocalDate localDate) {
        TotalTeacherResponse model = new TotalTeacherResponse();
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        LocalDate nowDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(nowDate.getYear(), nowDate.getMonthValue(), 1);
        LocalDate endDate = startDate.plusMonths(1);
        LocalDate date = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1).plusMonths(1);
        List<Long> idClassList = kidsClassDateRepository.findidClassList(idKid, idClass);
        int countDateKid = evaluateDateRepository.countSchoolUnreadOfMonthTeacher(idKid, idClass, startDate, endDate, date);
        int countWeekKid = evaluateWeekRepository.countSchoolUnreadTeacher(idSchool, idKid, idClass, idClassList, date);
        int countMonthKid = evaluateMonthRepository.countSchoolUnreadTeacher(idKid, idClass, idClassList, date);
        int countPeriodicKid = evaluatePeriodicRepository.countSchoolUnreadTeacher(idKid, idClass, date);
        model.setDateNumber(countDateKid);
        model.setWeekNumber(countWeekKid);
        model.setMonthNumber(countMonthKid);
        model.setPeriodicNumber(countPeriodicKid);
        return model;
    }

    @Override
    public List<EvaluateDateTeacherResponse> findEvaluateKisDate(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findEvaluateDateTeacher(principal.getIdClassLogin(), localDate);
        List<EvaluateDateTeacherResponse> dataList = new ArrayList<>();
        AttendanceConfigResponse attendanceConfigResponse = BeanDataUtils.getAttendanceConfigDate(principal.getIdSchoolLogin(), localDate);
        if (attendanceConfigResponse.isMorningAttendanceArrive() || attendanceConfigResponse.isAfternoonAttendanceArrive() || attendanceConfigResponse.isEveningAttendanceArrive()) {
            evaluateDateList.forEach(x -> {
                AttendanceKids attendanceKids = attendanceKidsRepository.findByAttendanceDateAndKidsId(localDate, x.getKids().getId()).orElseThrow(() -> new NotFoundException("not found attendanceKids by id in teacher"));
                EvaluateDateTeacherResponse model = new EvaluateDateTeacherResponse();
                if (StringUtils.isNotBlank(x.getLearnContent()) || StringUtils.isNotBlank(x.getEatContent()) || StringUtils.isNotBlank(x.getSleepContent()) || StringUtils.isNotBlank(x.getSanitaryContent()) || StringUtils.isNotBlank(x.getHealtContent()) || StringUtils.isNotBlank(x.getCommonContent()) || !CollectionUtils.isEmpty(x.getEvaluateAttachFileList())) {
                    model.setStatus(AppConstant.APP_TRUE);
                }
                model.setId(x.getId());
                model.setKidName(x.getKids().getFullName());
                model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
                model.setApproved(x.isApproved());
                model.setGoSchoolStatus(attendanceKids.getAttendanceArriveKids().isMorning() || attendanceKids.getAttendanceArriveKids().isAfternoon() || attendanceKids.getAttendanceArriveKids().isEvening() ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                //set trạng thái đi học
                if (!attendanceKids.isAttendanceArrive()) {
                    model.setAttendanceStatus(AttendanceConstant.TYPE_NO_ATTENDANCE);
                } else if (attendanceKids.getAttendanceArriveKids().isMorning() || attendanceKids.getAttendanceArriveKids().isAfternoon() || attendanceKids.getAttendanceArriveKids().isEvening()) {
                    model.setAttendanceStatus(AttendanceConstant.TYPE_GO_SCHOOL);
                } else {
                    model.setAttendanceStatus(AttendanceConstant.TYPE_ABSENT);
                }
                model.setLearnContent(x.getLearnContent());
                model.setEatContent(x.getEatContent());
                model.setSleepContent(x.getSleepContent());
                model.setSanitaryContent(x.getSanitaryContent());
                model.setHealtContent(x.getHealtContent());
                model.setCommonContent(x.getCommonContent());
                model.setFileList(listMapper.mapList(x.getEvaluateAttachFileList(), MobileFileTeacher.class));
                dataList.add(model);
            });
        }
        return dataList;
    }

//    private String getAttendanceStatus(AttendanceKids attendanceKids){
//        String status="";
//        AttendanceArriveKids attendanceArriveKids=attendanceKids.getAttendanceArriveKids();
//        if(!attendanceKids.isAttendanceArrive()){
//            status=AttendanceConstant.NO_ATTENDANCE;
//        }
//        return status;
//    }


    @Transactional
    @Override
    public ListEvaluateDateCreateTeacherResponse createEvaluateDate(UserPrincipal principal, ListEvaluateDateCreateTeacherRequest listEvaluateDateCreateTeacherRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        ListEvaluateDateCreateTeacherResponse response = this.checkBeforeCreateEvaluateDate(listEvaluateDateCreateTeacherRequest);
        if (CollectionUtils.isEmpty(response.getDataList())) {
            Long idUser = principal.getId();
            String fullName = principal.getFullName();
            LocalDateTime localDateTime = LocalDateTime.now();
            Long idSchool = principal.getIdSchoolLogin();
            List<EvaluateDate> evaluateDateList = new ArrayList<>();
            ConfigNotifyPlus configNotifyPlus = configNotifyPlusRepository.findBySchoolId(idSchool).orElseThrow();
            int failNumber = 0;
            boolean sendFirebasePlus = false;
            for (EvaluateDateCreateTeacherRequest x : listEvaluateDateCreateTeacherRequest.getDataList()) {
                EvaluateDate evaluateDateDB = evaluateDateRepository.findById(x.getId()).orElseThrow();
                boolean checkChange = this.checkChangeDate(evaluateDateDB, x);
                //nhận xét có sự thay đổi so với database
                if (checkChange) {
                    boolean checkHas = EvaluateUtil.checkHasEvaluateDate(evaluateDateDB);
                    //không nhận xét những học sinh đã duyệt và config là ko được duyệt
                    if (checkHas && !principal.getSchoolConfig().isEditAproved() && evaluateDateDB.isApproved()) {
                        failNumber++;
                    } else {
                        this.checkBeforeEditDate(evaluateDateDB, x);
                        if (!x.getLearnContent().equals(evaluateDateDB.getLearnContent())) {
                            evaluateDateDB.setLearnContent(x.getLearnContent());
                            evaluateDateDB.setLearnIdCreated(idUser);
                            evaluateDateDB.setLearnCreatedBy(fullName);
                            evaluateDateDB.setLearnDatetime(localDateTime);
                        }
                        if (!x.getEatContent().equals(evaluateDateDB.getEatContent())) {
                            evaluateDateDB.setEatContent(x.getEatContent());
                            evaluateDateDB.setEatIdCreated(idUser);
                            evaluateDateDB.setEatCreatedBy(fullName);
                            evaluateDateDB.setEatDatetime(localDateTime);
                        }
                        if (!x.getSleepContent().equals(evaluateDateDB.getSleepContent())) {
                            evaluateDateDB.setSleepContent(x.getSleepContent());
                            evaluateDateDB.setSleepIdCreated(idUser);
                            evaluateDateDB.setSleepCreatedBy(fullName);
                            evaluateDateDB.setSleepDatetime(localDateTime);
                        }
                        if (!x.getSanitaryContent().equals(evaluateDateDB.getSanitaryContent())) {
                            evaluateDateDB.setSanitaryContent(x.getSanitaryContent());
                            evaluateDateDB.setSanitaryIdCreated(idUser);
                            evaluateDateDB.setSanitaryCreatedBy(fullName);
                            evaluateDateDB.setSanitaryDatetime(localDateTime);
                        }
                        if (!x.getHealtContent().equals(evaluateDateDB.getHealtContent())) {
                            evaluateDateDB.setHealtContent(x.getHealtContent());
                            evaluateDateDB.setHealtIdCreated(idUser);
                            evaluateDateDB.setHealtCreatedBy(fullName);
                            evaluateDateDB.setHealtDatetime(localDateTime);
                        }
                        if (!x.getCommonContent().equals(evaluateDateDB.getCommonContent())) {
                            evaluateDateDB.setCommonContent(x.getCommonContent());
                            evaluateDateDB.setCommonIdCreated(idUser);
                            evaluateDateDB.setCommonCreatedBy(fullName);
                            evaluateDateDB.setCommonDatetime(localDateTime);
                        }
                        //check tạo lần đầu
                        if (evaluateDateDB.getIdCreated() == null || evaluateDateDB.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
                            evaluateDateDB.setIdCreated(idUser);
                            evaluateDateDB.setCreatedDate(localDateTime);
                            evaluateDateDB.setIdModified(idUser);
                            evaluateDateDB.setLastModifieDate(localDateTime);
                            evaluateDateDB.setApproved(principal.getSchoolConfig().isEvaluate());
                            if (principal.getSchoolConfig().isEvaluate()) {
                                firebaseFunctionService.sendParentByTeacherNoContent(38L, evaluateDateDB.getKids(), FirebaseConstant.CATEGORY_EVALUATE, "");
                            } else {
                                sendFirebasePlus = true;
                            }
                        } else {
                            evaluateDateDB.setIdModified(idUser);
                            evaluateDateDB.setLastModifieDate(localDateTime);
                        }
                        EvaluateDate evaluateDateSaved = evaluateDateRepository.save(evaluateDateDB);
                        this.saveFileDate(x.getMultipartFileList(), evaluateDateSaved, idSchool);
                        this.deleteFileDate(x.getIdFileDeleteList());
                        evaluateDateList.add(evaluateDateDB);
                    }
                }
            }
            if (sendFirebasePlus) {
                this.sendFirebaseNewEvaluate("Nhận xét ngày", "Có nhận xét ngày của giáo viên cần xác nhận");
            }
            response.setFailNumber(failNumber);
        }
        return response;
    }

    @Override
    public List<Integer> findEvaluateDateOfMonth(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        List<Integer> integerList = new ArrayList<>();
        LocalDate startDate = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1);
        LocalDate endDate = startDate.plusMonths(1);
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findEvaluateDateOfMonthTeacher(principal.getIdClassLogin(), startDate, endDate);
        int count = 1;
        do {
            int finalCount = count;
            List<EvaluateDate> evaluateDateOfDayList = evaluateDateList.stream().filter(x -> x.getDate().getDayOfMonth() == finalCount).collect(Collectors.toList());
            for (EvaluateDate x : evaluateDateOfDayList) {
                if (StringUtils.isNotBlank(x.getLearnContent()) || StringUtils.isNotBlank(x.getEatContent()) || StringUtils.isNotBlank(x.getSleepContent())
                        || StringUtils.isNotBlank(x.getSanitaryContent()) || StringUtils.isNotBlank(x.getHealtContent()) || StringUtils.isNotBlank(x.getCommonContent())
                        || !CollectionUtils.isEmpty(x.getEvaluateAttachFileList())) {
                    integerList.add(count);
                    break;
                }
            }
            count++;
        } while (count <= 31);
        return integerList;
    }

    @Override
    public List<EvaluateTeacherResponse> findEvaluateKidsWeek(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        Long idClassLogin = principal.getIdClassLogin();
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findByEvaluateWeekWidthClass(idClassLogin, localDate);
        List<EvaluateTeacherResponse> dataList = new ArrayList<>();
        evaluateWeekList.forEach(x -> {
            EvaluateTeacherResponse model = new EvaluateTeacherResponse();
            model.setId(x.getId());
            model.setKidsStatus(this.getKidsStatus(x.getKids(), idClassLogin));
            model.setStatus(StringUtils.isNotBlank(x.getContent()) || !CollectionUtils.isEmpty(x.getEvaluateWeekFileList()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            model.setKidName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setApproved(x.isApproved());
            model.setContent(x.getContent());
            model.setFileList(listMapper.mapList(x.getEvaluateWeekFileList(), MobileFileTeacher.class));
            dataList.add(model);
        });
        return dataList;
    }

    @Transactional
    @Override
    public ListEvaluateCreateTeacherResponse createEvaluateWeek(UserPrincipal principal, ListEvaluateCreateTeacherRequest listEvaluateCreateTeacherRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        ListEvaluateCreateTeacherResponse response = this.checkBeforeCreateWeek(listEvaluateCreateTeacherRequest);
        if (CollectionUtils.isEmpty(response.getDataList())) {
            Long idUser = principal.getId();
            String fullName = principal.getFullName();
            LocalDateTime localDateTime = LocalDateTime.now();
            Long idSchool = principal.getIdSchoolLogin();
            List<EvaluateWeek> evaluateWeeksList = new ArrayList<>();
            boolean sendFirebasePlus = false;
            int failNumber = 0;
            for (EvaluateCreateTeacherRequest x : listEvaluateCreateTeacherRequest.getDataList()) {
                EvaluateWeek evaluateWeek = evaluateWeekRepository.findById(x.getId()).orElseThrow();
                boolean checkChange = this.checkChangeWeek(evaluateWeek, x);
                if (checkChange) {
                    boolean checkHas = EvaluateUtil.checkHasEvaluateWeek(evaluateWeek);
                    //chưa có nx, chưa duyệt hoặc đã duyệt nhưng cho sửa
                    if (checkHas && !principal.getSchoolConfig().isEditAproved() && evaluateWeek.isApproved()) {
                        failNumber++;
                    } else {
                        this.checkBeforeEditWeek(evaluateWeek, x);
                        if (!x.getContent().equals(evaluateWeek.getContent())) {
                            evaluateWeek.setContent(x.getContent());
                            evaluateWeek.setIdModified(idUser);
                            evaluateWeek.setLastModifieBy(fullName);
                            evaluateWeek.setLastModifieDate(localDateTime);
                        }
                        //check lần đầu tạo nhận xét
                        if (evaluateWeek.getIdCreated() == null || evaluateWeek.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
                            evaluateWeek.setIdCreated(idUser);
                            evaluateWeek.setCreatedDate(localDateTime);
                            evaluateWeek.setIdModified(idUser);
                            evaluateWeek.setLastModifieDate(localDateTime);
                            evaluateWeek.setApproved(principal.getSchoolConfig().isEvaluate());
                            if (principal.getSchoolConfig().isEvaluate()) {
                                firebaseFunctionService.sendParentByTeacherNoContent(42L, evaluateWeek.getKids(), FirebaseConstant.CATEGORY_EVALUATE, "");
                            } else {
                                sendFirebasePlus = true;
                            }
                        }
                        EvaluateWeek evaluateWeekSaved = evaluateWeekRepository.save(evaluateWeek);
                        this.saveFileWeek(x.getMultipartFileList(), evaluateWeekSaved, idSchool);
                        this.deleteFileWeek(x.getIdFileDeleteList());
                        evaluateWeeksList.add(evaluateWeek);
                    }
                }
            }
            if (sendFirebasePlus) {
                this.sendFirebaseNewEvaluate("Nhận xét tuần", "Có nhận xét tuần của giáo viên cần xác nhận");
            }
            response.setFailNumber(failNumber);
        }
        return response;
    }

    @Override
    public List<EvaluateTeacherResponse> findEvaluateKidsMonth(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findByEvaluateMonthWithClass(principal.getIdClassLogin(), month, year);
        List<EvaluateTeacherResponse> dataList = new ArrayList<>();
        evaluateMonthList.forEach(x -> {
            EvaluateTeacherResponse model = new EvaluateTeacherResponse();
            model.setId(x.getId());
            model.setKidsStatus(this.getKidsStatus(x.getKids(), principal.getIdClassLogin()));
            model.setStatus(StringUtils.isNotBlank(x.getContent()) || !CollectionUtils.isEmpty(x.getEvaluateMonthFileList()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            model.setKidName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setApproved(x.isApproved());
            model.setContent(x.getContent());
            model.setFileList(listMapper.mapList(x.getEvaluateMonthFileList(), MobileFileTeacher.class));
            dataList.add(model);
        });
        return dataList;
    }

    @Transactional
    @Override
    public ListEvaluateCreateTeacherResponse createEvaluateMonth(UserPrincipal principal, ListEvaluateCreateTeacherRequest listEvaluateCreateTeacherRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        ListEvaluateCreateTeacherResponse response = this.checkBeforeCreateMonth(listEvaluateCreateTeacherRequest);
        if (CollectionUtils.isEmpty(response.getDataList())) {
            Long idUser = principal.getId();
            String fullName = principal.getFullName();
            LocalDateTime localDateTime = LocalDateTime.now();
            Long idSchool = principal.getIdSchoolLogin();
            List<EvaluateMonth> evaluateMonthList = new ArrayList<>();
            int failNumber = 0;
            boolean sendFirebasePlus = false;
            for (EvaluateCreateTeacherRequest x : listEvaluateCreateTeacherRequest.getDataList()) {
                EvaluateMonth evaluateMonth = evaluateMonthRepository.findById(x.getId()).orElseThrow();
                boolean checkChange = this.checkChangeMonth(evaluateMonth, x);
                if (checkChange) {
                    boolean checkHas = EvaluateUtil.checkHasEvaluateMonth(evaluateMonth);
                    //không nhận xét những học sinh đã duyệt và config là ko được duyệt
                    if (checkHas && !principal.getSchoolConfig().isEditAproved() && evaluateMonth.isApproved()) {
                        failNumber++;
                    } else {
                        this.checkBeforeEditMonth(evaluateMonth, x);
                        if (!x.getContent().equals(evaluateMonth.getContent())) {
                            evaluateMonth.setContent(x.getContent());
                            evaluateMonth.setIdModified(idUser);
                            evaluateMonth.setLastModifieBy(fullName);
                            evaluateMonth.setLastModifieDate(localDateTime);
                        }
                        //check tạo lần đầu
                        if (evaluateMonth.getIdCreated() == null || evaluateMonth.getIdCreated().equals(AppConstant.NUMBER_ZERO)) {
                            evaluateMonth.setIdCreated(idUser);
                            evaluateMonth.setCreatedDate(localDateTime);
                            evaluateMonth.setIdModified(idUser);
                            evaluateMonth.setLastModifieDate(localDateTime);
                            evaluateMonth.setApproved(principal.getSchoolConfig().isEvaluate());
                            if (principal.getSchoolConfig().isEvaluate()) {
                                firebaseFunctionService.sendParentByTeacherNoContent(46L, evaluateMonth.getKids(), FirebaseConstant.CATEGORY_EVALUATE, "");
                            } else {
                                sendFirebasePlus = true;
                            }
                        } else {
                            evaluateMonth.setIdModified(idUser);
                            evaluateMonth.setLastModifieDate(localDateTime);
                        }
                        EvaluateMonth evaluateMonthSaved = evaluateMonthRepository.save(evaluateMonth);
                        this.saveFileMonth(x.getMultipartFileList(), evaluateMonthSaved, idSchool);
                        this.deleteFileMonth(x.getIdFileDeleteList());
                        evaluateMonthList.add(evaluateMonth);
                    }
                }
            }
            if (sendFirebasePlus) {
                this.sendFirebaseNewEvaluate("Nhận xét tháng", "Có nhận xét tháng của giáo viên cần xác nhận");
            }
            response.setFailNumber(failNumber);
        }
        return response;
    }

    private void sendFirebaseNewEvaluate(String title, String content) throws FirebaseMessagingException {
        List<InfoEmployeeSchool> plusList = UserInforUtils.getPlusInSchoolHasAccount();
        firebaseFunctionService.sendPlusCommon(plusList, title, content, SchoolUtils.getIdSchool(), FirebaseConstant.CATEGORY_EVALUATE);
    }

    //    fireBasse
    private void sendFireBaseMonth(Kids kids, Long idTitle) throws FirebaseMessagingException {
        Optional<WebSystemTitle> webSystemTitle = webSystemTitleService.findById(idTitle);
        String title = webSystemTitle.get().getTitle();
        if (kids.getParent() != null) {
            String content = webSystemTitle.get().getContent().replace("{Kid_Name}", kids.getFullName()).replace("{dd/mm/yyyy}", LocalDate.now().toString());
            List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(kids.getParent());
            if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                NotifyRequest notifyRequest = new NotifyRequest();
                notifyRequest.setBody(content);
                notifyRequest.setTitle(title);
                FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.EVALUATE, notifyRequest, kids.getId().toString());
            }
        }
    }

    @Override
    public List<EvaluatePeriodicTeacherResponse> findEvaluateKidsPeriodic(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        Long idClass = principal.getIdClassLogin();
        List<EvaluatePeriodicTeacherResponse> dataList = new ArrayList<>();
        LocalDate nowDate = LocalDate.now();
        //chọn ngày quá khứ
        if (nowDate.isAfter(localDate)) {
            List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findByEvaluatePeriodicWithClass(idClass, localDate);
            evaluatePeriodicList.forEach(x -> {
                EvaluatePeriodicTeacherResponse model = this.setModelEvalutePeridoic(x);
                dataList.add(model);
            });
        } else if (nowDate.isEqual(localDate)) {
            //ngày hiện tại
            List<Kids> kidsList = kidsRepository.findByKidsClassWithStatus(principal.getIdClassLogin(), StudentStatusEnum.STUDYING.toString());
            kidsList.forEach(x -> {
                EvaluatePeriodicTeacherResponse model;
                Optional<EvaluatePeriodic> evaluatePeriodicOptional = evaluatePeriodicRepository.findByDateAndKidsId(LocalDate.now(), x.getId());
                //đã có nhận xét định kì
                if (evaluatePeriodicOptional.isPresent()) {
                    model = this.setModelEvalutePeridoic(evaluatePeriodicOptional.get());
                } else {
                    //chưa có nhận xét định kỳ
                    model = new EvaluatePeriodicTeacherResponse();
                    model.setDateTime(LocalDateTime.now());
                    model.setIdKid(x.getId());
                    model.setKidName(x.getFullName());
                    model.setAvatar(ConvertData.getAvatarKid(x));
                    model.setContent("");
                    model.setFileList(new ArrayList<>());
                }
                dataList.add(model);
            });
        }
        return dataList;
    }

    @Transactional
    @Override
    public ListEvaluateCreateTeacherResponse createEvaluatePeriodic(UserPrincipal principal, ListEvaluatePeriodicCreateTeacherRequest listEvaluatePeriodicCreateTeacherRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        ListEvaluateCreateTeacherResponse response = this.checkBeforeCreatePeriodic(listEvaluatePeriodicCreateTeacherRequest);
        if (CollectionUtils.isEmpty(response.getDataList())) {
            Long idUser = principal.getId();
            String fullName = principal.getFullName();
            LocalDateTime localDateTime = LocalDateTime.now();
            Long idSchool = principal.getIdSchoolLogin();
            List<EvaluatePeriodic> evaluatePeriodicList = new ArrayList<>();
            LocalDate nowDate = LocalDate.now();
            int failNumber = 0;
            boolean sendFirebasePlus = false;
            for (EvaluatePeriodicCreateTeacherRequest x : listEvaluatePeriodicCreateTeacherRequest.getDataList()) {
                Long idEvaluatePeriodic = x.getId();
                if (StringUtils.isNotBlank(x.getContent()) || !CollectionUtils.isEmpty(x.getMultipartFileList())) {
                    EvaluatePeriodic evaluatePeriodic = null;
                    Optional<EvaluatePeriodic> evaluatePeriodicOptional = evaluatePeriodicRepository.findByDateAndKidsId(nowDate, x.getIdKid());
                    //chưa được tạo nhận xét định kỳ cho ngày hiện tại
                    if (idEvaluatePeriodic == null && evaluatePeriodicOptional.isEmpty()) {
                        evaluatePeriodic = new EvaluatePeriodic();
                        Kids kid = kidsRepository.findById(x.getIdKid()).orElseThrow();
                        evaluatePeriodic.setIdSchool(kid.getIdSchool());
                        evaluatePeriodic.setIdGrade(kid.getIdGrade());
                        evaluatePeriodic.setIdClass(kid.getMaClass().getId());
                        evaluatePeriodic.setDate(nowDate);
                        evaluatePeriodic.setKids(kid);
                        evaluatePeriodic.setApproved(principal.getSchoolConfig().isEvaluate());
                        evaluatePeriodic.setContent(x.getContent() != null ? x.getContent() : "");
                        if (principal.getSchoolConfig().isEvaluate()) {
                            firebaseFunctionService.sendParentByTeacherNoContent(50L, evaluatePeriodic.getKids(), FirebaseConstant.CATEGORY_EVALUATE, "");
                        } else {
                            sendFirebasePlus = true;
                        }
                    } else {
                        //đã có nhận xét định kỳ cho ngày hiện tại
                        if (idEvaluatePeriodic != null) {
                            evaluatePeriodic = evaluatePeriodicRepository.findById(x.getId()).orElseThrow();
                        } else if (evaluatePeriodicOptional.isPresent()) {
                            evaluatePeriodic = evaluatePeriodicOptional.get();
                        }
                        boolean checkChange = this.checkChangePeriodic(evaluatePeriodic, x);
                        if (checkChange) {
                            //không nhận xét những học sinh đã duyệt và config là ko được duyệt
                            if (!principal.getSchoolConfig().isEditAproved() && evaluatePeriodic.isApproved()) {
                                failNumber++;
                            } else {
                                this.checkBeforeEditPeriodic(evaluatePeriodic, x);
                                if (!x.getContent().equals(evaluatePeriodic.getContent())) {
                                    evaluatePeriodic.setContent(x.getContent());
                                }
                            }
                            evaluatePeriodic.setIdModified(idUser);
                            evaluatePeriodic.setLastModifieBy(fullName);
                            evaluatePeriodic.setLastModifieDate(localDateTime);
                        }
                        this.deleteFilePeriodic(x.getIdFileDeleteList());
                    }
                    EvaluatePeriodic evaluatePeriodicSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
                    this.saveFilePeriodic(x.getMultipartFileList(), evaluatePeriodicSaved, idSchool);
                    evaluatePeriodicList.add(evaluatePeriodic);
                }
            }
            if (sendFirebasePlus) {
                this.sendFirebaseNewEvaluate("Nhận xét định kỳ", "Có nhận xét định kỳ của giáo viên cần xác nhận");
            }
            response.setFailNumber(failNumber);
        }
        return response;
    }

    //    fireBasse
    private void sendFireBasePeriodic(Kids kids, Long idTitle) throws FirebaseMessagingException {
        Optional<WebSystemTitle> webSystemTitle = webSystemTitleService.findById(idTitle);

        String title = webSystemTitle.get().getTitle();

        if (kids.getParent() != null) {
            String content = webSystemTitle.get().getContent().replace("{Kid_Name}", kids.getFullName()).replace("{dd/mm/yyyy}", LocalDate.now().toString());
            List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(kids.getParent());
            if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                NotifyRequest notifyRequest = new NotifyRequest();
                notifyRequest.setBody(content);
                notifyRequest.setTitle(title);
                FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.EVALUATE, notifyRequest, kids.getId().toString());
            }
        }

    }

    @Override
    public List<Integer> statisticalPeriodicOfMonthDate(UserPrincipal principal, LocalDate localDate) {
        int dayNumberOfMonth = ConvertData.getDateNumberOfMonth(localDate);
        Long idClass = principal.getIdClassLogin();
        List<Integer> integerList = new ArrayList<>();
        for (int i = 1; i <= dayNumberOfMonth; i++) {
            LocalDate date = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), i);
            if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())) {
                List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findByIdClassAndDate(idClass, date);
                if (!CollectionUtils.isEmpty(evaluatePeriodicList)) {
                    integerList.add(i);
                }
            }
        }
        return integerList;
    }


    @Override
    public List<StatisticalOfMonthTeacherResponse> statisticalOfMonth(UserPrincipal principal, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        LocalDate startDate = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1);
        LocalDate endDate = startDate.plusMonths(1);
        Long idClass = principal.getIdClassLogin();
        List<Long> idKidDateList = evaluateDateRepository.findIdKidOfMonthList(principal.getIdClassLogin(), startDate, endDate);
        List<Long> idKidMonthList = evaluateMonthRepository.findIdKidOfMonthList(idClass, localDate.getMonthValue(), localDate.getYear());
        List<Long> idKidList = ConvertData.intersecionList(idKidDateList, idKidMonthList);
        List<Kids> kidsList = kidsRepository.findKidsByIdList(idKidList);
        List<StatisticalOfMonthTeacherResponse> dataList = new ArrayList<>();
        kidsList.forEach(kids -> {
            StatisticalOfMonthTeacherResponse model = new StatisticalOfMonthTeacherResponse();
            model.setIdKid(kids.getId());
            model.setKidName(kids.getFullName());
            model.setKidsStatus(this.getKidsStatus(kids, principal.getIdClassLogin()));
            model.setAvatar(ConvertData.getAvatarKid(kids));
            this.setProperties(model, idClass, kids.getId(), startDate, endDate);
            dataList.add(model);
        });
        return dataList;
    }

    @Override
    public List<Integer> statisticalDateAndPeriodic(UserPrincipal principal, LocalDate localDate) {
        List<Integer> dateList = new ArrayList<>();
        Long idClass = principal.getIdClassLogin();
        int daysInMonth = ConvertData.getDateNumberOfMonth(localDate);
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), i);
            if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())) {
                List<EvaluateDate> evaluateDateList = evaluateDateRepository.findEvaluateClassDate(idClass, date);
                List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findByIdClassAndDate(idClass, date);
                if (!CollectionUtils.isEmpty(evaluateDateList) || !CollectionUtils.isEmpty(evaluatePeriodicList)) {
                    dateList.add(i);
                }
            }
        }
        return dateList;
    }

    @Override
    public EvaluateDateKidTeacherResponse getEvaluateDateKid(UserPrincipal principal, Long idKid, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        Long idClass = principal.getIdClassLogin();
        EvaluateDateKidTeacherResponse model = new EvaluateDateKidTeacherResponse();
        Long idSchool = principal.getIdSchoolLogin();
        List<Long> idClassList = kidsClassDateRepository.findidClassList(idKid, idClass);
        Optional<EvaluateDate> evaluateDateOptional = Optional.empty();
        if (localDate == null) {
            evaluateDateOptional = evaluateDateRepository.findEvaluateDateHas(idClass, idClassList, idKid);
        }
        if (evaluateDateOptional.isEmpty()) {
            localDate = localDate == null ? LocalDate.now() : localDate;
            evaluateDateOptional = evaluateDateRepository.findEvaluateDateOfDate(principal.getIdSchoolLogin(), idClass, principal.getSchoolConfig().isHistoryViewParent(), idClassList, idKid, localDate);
        }
        if (evaluateDateOptional.isPresent()) {
            EvaluateDate evaluateDate = evaluateDateOptional.get();
            model.setId(evaluateDate.getId());
            model.setDate(evaluateDate.getDate());
            model.setLearnContent(evaluateDate.getLearnContent());
            model.setEatContent(evaluateDate.getEatContent());
            model.setApproved(evaluateDate.isApproved());
            model.setSleepContent(evaluateDate.getSleepContent());
            model.setSanitaryContent(evaluateDate.getSanitaryContent());
            model.setHealtContent(evaluateDate.getHealtContent());
            model.setCommonContent(evaluateDate.getCommonContent());
            model.setFileList(listMapper.mapList(evaluateDate.getEvaluateAttachFileList(), AttachFileMobileResponse.class));
            this.setEvaluateDateReply(idSchool, evaluateDate, model, principal.getId());
            this.updateParentRead(evaluateDate);
        } else {
            model.setId(0l);
            model.setDate(localDate);
        }
        return model;
    }

    @Override
    public int countKidDateSchoolUnread(UserPrincipal principal, Long idKid, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        StartEndDateObject startEndDateObject = ConvertData.getStartEndDateOfMonth(localDate);
        int count = evaluateDateRepository.countSchoolUnreadKidOfMonth(principal.getIdClassLogin(), idKid, startEndDateObject);
        return count;
    }

    @Override
    public EvaluateDateHaveTeacherResponse findKidDateHaveOfMonth(UserPrincipal principal, Long idKid, LocalDate localDate) {
        CommonValidate.checkDataTeacher(principal);
        EvaluateDateHaveTeacherResponse response = new EvaluateDateHaveTeacherResponse();
        List<DateStatusObject> evaluateMonthList = new ArrayList<>();
        List<DateStatusObject> evaluateReplyMonthList = new ArrayList<>();

        StartEndDateObject startEndDateObject = ConvertData.getStartEndDateOfMonth(localDate);
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findKidDateHaveOfMonth(principal.getIdClassLogin(), idKid, startEndDateObject);
        evaluateDateList.forEach(x -> {
            int date = x.getDate().getDayOfMonth();
            DateStatusObject model = new DateStatusObject();
            model.setDate(date);
            model.setStatus(x.isApproved());
            evaluateMonthList.add(model);
        });
        List<EvaluateDate> evaluateDateReplyList = evaluateDateRepository.findKidDateHaveOfReplyMonth(principal.getIdClassLogin(), idKid, startEndDateObject);
        evaluateDateReplyList.forEach(x -> {
            int date = x.getDate().getDayOfMonth();
            DateStatusObject model = new DateStatusObject();
            model.setDate(date);
            model.setStatus(x.isSchoolReadReply());
            evaluateReplyMonthList.add(model);
        });
        response.setEvaluateMonthList(evaluateMonthList);
        response.setEvaluateReplyMonthList(evaluateReplyMonthList);
        return response;
    }

    @Transactional
    @Override
    public ReplyTypeEditObject createKidDateReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateDate evaluateDate = evaluateDateRepository.findById(request.getId()).orElseThrow();
        boolean checkSendFirebase = evaluateDate.isApproved() && evaluateDate.getTeacherReplyIdCreated() == null;
        this.setDateReplyData(principal, evaluateDate, request);
        EvaluateDate evaluateDateSaved = evaluateDateRepository.save(evaluateDate);
        if (checkSendFirebase) {
            //gửi firebase
            firebaseFunctionService.sendParentByTeacher(40L, evaluateDate.getKids(), FirebaseConstant.CATEGORY_EVALUATE, request.getContent());
        }
        ReplyTypeEditObject model = this.setDateReplyResponse(idSchool, evaluateDateSaved, principal.getId());
        return model;
    }

    @Override
    public ReplyTypeEditObject revokeKidDateReplye(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateDate evaluateDate = evaluateDateRepository.findById(id).orElseThrow();
        evaluateDate.setTeacherReplyDel(AppConstant.APP_TRUE);
        evaluateDate.setParentRead(AppConstant.APP_FALSE);
        EvaluateDate evaluateDateSaved = evaluateDateRepository.save(evaluateDate);
        ReplyTypeEditObject model = this.setDateReplyResponse(idSchool, evaluateDateSaved, principal.getId());
        return model;
    }

    @Override
    public ListEvaluateKidTeacherResponse getEvaluateWeekKid(UserPrincipal principal, KidsPageNumberRequest request) {
        CommonValidate.checkDataTeacher(principal);
        ListEvaluateKidTeacherResponse response = new ListEvaluateKidTeacherResponse();
        Long idClassLogin = principal.getIdClassLogin();
        Long idSchool = principal.getIdSchoolLogin();
        List<Long> idClassList = kidsClassDateRepository.findidClassList(request.getIdKid(), idClassLogin);
        //todo historyView đang sai, trường cũ cho xem hay không chứ phải ko phải trường mới cho xem hay không
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findEvaluateWeekKidAndPaging(principal.getIdSchoolLogin(), idClassLogin, idClassList, principal.getSchoolConfig().isHistoryViewParent(), request);
        List<EvaluateKidTeacherResponse> dataList = new ArrayList<>();
        evaluateWeekList.forEach(x -> {
            EvaluateKidTeacherResponse model = new EvaluateKidTeacherResponse();
            model.setId(x.getId());
            model.setName(ConvertData.convertDateToWeek(x.getWeek(), x.getDate()));
            model.setApproved(x.isApproved());
            model.setContent(x.getContent());
            model.setReadStatus(!x.isSchoolReadReply() && StringUtils.isNotBlank(x.getParentReplyContent()));
            model.setSameClass(x.getIdClass().equals(idClassLogin));
            model.setFileList(listMapper.mapList(x.getEvaluateWeekFileList(), AttachFileMobileResponse.class));
            this.setWeekReply(idSchool, x, model, principal.getId());
            dataList.add(model);
        });
        response.setDataList(dataList);
        response.setLastPage(dataList.size() < MobileConstant.MAX_PAGE_ITEM);
        return response;
    }

    @Override
    public boolean viewEvaluateWeekKid(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        EvaluateWeek evaluateWeek = evaluateWeekRepository.findById(id).orElseThrow();
        evaluateWeek.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateWeekRepository.save(evaluateWeek);
        return true;
    }

    @Transactional
    @Override
    public ReplyTypeEditObject createKidWeekReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws
            FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateWeek evaluate = evaluateWeekRepository.findById(request.getId()).orElseThrow();
        boolean checkSendFirebase = evaluate.isApproved() && evaluate.getTeacherReplyIdCreated() == null;
        this.setWeekReplyData(principal, evaluate, request);
        EvaluateWeek evaluateDateSaved = evaluateWeekRepository.save(evaluate);
        if (checkSendFirebase) {
            //gửi firebase
            firebaseFunctionService.sendParentByTeacher(44L, evaluate.getKids(), FirebaseConstant.CATEGORY_EVALUATE, request.getContent());
        }
        ReplyTypeEditObject model = this.setWeekReplyResponse(idSchool, evaluateDateSaved, principal.getId());


        return model;
    }

    @Override
    public ReplyTypeEditObject revokeKidWeekReplye(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateWeek evaluate = evaluateWeekRepository.findById(id).orElseThrow();
        evaluate.setTeacherReplyDel(AppConstant.APP_TRUE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
        EvaluateWeek evaluateSaved = evaluateWeekRepository.save(evaluate);
        ReplyTypeEditObject model = this.setWeekReplyResponse(idSchool, evaluateSaved, principal.getId());
        return model;
    }

    @Override
    public ListEvaluateKidTeacherResponse getEvaluateMonthKid(UserPrincipal principal, KidsPageNumberRequest
            request) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClassLogin = principal.getIdClassLogin();
        ListEvaluateKidTeacherResponse response = new ListEvaluateKidTeacherResponse();
        List<Long> idClassList = kidsClassDateRepository.findidClassList(request.getIdKid(), idClassLogin);
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findEvaluateMonthKidAndPaging(principal.getIdSchoolLogin(), idClassLogin, idClassList, principal.getSchoolConfig().isHistoryViewParent(), request);
        List<EvaluateKidTeacherResponse> dataList = new ArrayList<>();
        evaluateMonthList.forEach(x -> {
            EvaluateKidTeacherResponse model = new EvaluateKidTeacherResponse();
            model.setId(x.getId());
            model.setName(ConvertData.convertDateToMonth(x.getMonth(), x.getYear()));
            model.setApproved(x.isApproved());
            model.setContent(x.getContent());
            model.setReadStatus(!x.isSchoolReadReply() && StringUtils.isNotBlank(x.getParentReplyContent()));
            model.setSameClass(x.getIdClass().equals(idClassLogin));
            model.setFileList(listMapper.mapList(x.getEvaluateMonthFileList(), AttachFileMobileResponse.class));
            this.setMonthReply(idSchool, x, model, principal.getId());
            dataList.add(model);
        });
        response.setDataList(dataList);
        response.setLastPage(dataList.size() < MobileConstant.MAX_PAGE_ITEM);
        return response;
    }

    @Override
    public boolean viewEvaluateMonthKid(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        EvaluateMonth evaluateMonth = evaluateMonthRepository.findById(id).orElseThrow();
        evaluateMonth.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateMonthRepository.save(evaluateMonth);
        return true;
    }

    @Override
    public ReplyTypeEditObject createKidMonthReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws
            FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateMonth evaluate = evaluateMonthRepository.findById(request.getId()).orElseThrow();
        boolean checkSendFirebase = evaluate.isApproved() && evaluate.getTeacherReplyIdCreated() == null;
        this.setMonthReplyData(principal, evaluate, request);
        EvaluateMonth evaluateMonthSaved = evaluateMonthRepository.save(evaluate);
        if (checkSendFirebase) {
            //gửi firebase
            firebaseFunctionService.sendParentByTeacher(48L, evaluate.getKids(), FirebaseConstant.CATEGORY_EVALUATE, request.getContent());
        }
        ReplyTypeEditObject model = this.setMonthReplyResponse(idSchool, evaluateMonthSaved, principal.getId());


        return model;
    }

    @Override
    public ReplyTypeEditObject revokeKidMonthReply(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateMonth evaluate = evaluateMonthRepository.findById(id).orElseThrow();
        evaluate.setTeacherReplyDel(AppConstant.APP_TRUE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
        EvaluateMonth evaluateSaved = evaluateMonthRepository.save(evaluate);
        ReplyTypeEditObject model = this.setMonthReplyResponse(idSchool, evaluateSaved, principal.getId());
        return model;
    }

    @Override
    public ListEvaluateKidTeacherResponse getEvaluatePeriodicKid(UserPrincipal principal, KidsPageNumberRequest
            request) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idClassLogin = principal.getIdClassLogin();
        ListEvaluateKidTeacherResponse response = new ListEvaluateKidTeacherResponse();
        List<Long> idClassList = kidsClassDateRepository.findidClassList(request.getIdKid(), idClassLogin);
        List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findEvaluatePeriodicKidAndPaging(principal.getIdSchoolLogin(), idClassLogin, idClassList, principal.getSchoolConfig().isHistoryViewParent(), request);
        List<EvaluateKidTeacherResponse> dataList = new ArrayList<>();
        evaluatePeriodicList.forEach(x -> {
            EvaluateKidTeacherResponse model = new EvaluateKidTeacherResponse();
            model.setId(x.getId());
            model.setName(ConvertData.convertDateToPeriodic(x.getDate()));
            model.setApproved(x.isApproved());
            model.setContent(x.getContent());
            model.setReadStatus(!x.isSchoolReadReply() && StringUtils.isNotBlank(x.getParentReplyContent()));
            model.setSameClass(x.getIdClass().equals(idClassLogin));
            model.setFileList(listMapper.mapList(x.getEvaluatePeriodicFileList(), AttachFileMobileResponse.class));
            this.setPeriodicReply(idSchool, x, model, principal.getId());
            dataList.add(model);
        });
        response.setDataList(dataList);
        response.setLastPage(dataList.size() < MobileConstant.MAX_PAGE_ITEM);
        return response;
    }

    @Override
    public boolean viewEvaluatePeriodicKid(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findById(id).orElseThrow();
        evaluatePeriodic.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluatePeriodicRepository.save(evaluatePeriodic);
        return true;
    }

    @Override
    public ReplyTypeEditObject createKidPeriodicReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws
            FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluatePeriodic evaluate = evaluatePeriodicRepository.findById(request.getId()).orElseThrow();
        boolean checkSendFirebase = evaluate.isApproved() && evaluate.getTeacherReplyIdCreated() == null;
        this.setPeriodicReplyData(principal, evaluate, request);
        EvaluatePeriodic evaluateSaved = evaluatePeriodicRepository.save(evaluate);
        if (checkSendFirebase) {
            //gửi firebase
            firebaseFunctionService.sendParentByTeacher(52L, evaluate.getKids(), FirebaseConstant.CATEGORY_EVALUATE, request.getContent());
        }
        ReplyTypeEditObject model = this.setPeriodicReplyResponse(idSchool, evaluateSaved, principal.getId());


        return model;
    }

    @Override
    public ReplyTypeEditObject revokeKidPeriodicReply(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluatePeriodic evaluate = evaluatePeriodicRepository.findById(id).orElseThrow();
        evaluate.setTeacherReplyDel(AppConstant.APP_TRUE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
        EvaluatePeriodic evaluateSaved = evaluatePeriodicRepository.save(evaluate);
        ReplyTypeEditObject model = this.setPeriodicReplyResponse(idSchool, evaluateSaved, principal.getId());
        return model;
    }

    @Override
    public EvaluateStatusResponse getEvaluateStatus(UserPrincipal principal) {
        CommonValidate.checkDataTeacher(principal);
        EvaluateStatusResponse model = new EvaluateStatusResponse();
        Long idClass = principal.getIdClassLogin();
        LocalDate nowDate = LocalDate.now();
        LocalDate weekDate = ConvertData.getMondayOfWeek(nowDate);
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findByIdClassAndDate(idClass, nowDate);
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findByIdClassAndDate(idClass, weekDate);
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findByIdClassAndMonthAndYear(idClass, nowDate.getMonthValue(), nowDate.getYear());
        int countDate = 0;
        int countWeek = 0;
        int countMonth = 0;
        for (EvaluateDate x : evaluateDateList) {
            if (EvaluateUtil.checkHasEvaluateDate(x)) {
                countDate++;
            }
        }
        for (EvaluateWeek x : evaluateWeekList) {
            if (EvaluateUtil.checkHasEvaluateWeek(x)) {
                countWeek++;
            }
        }
        for (EvaluateMonth x : evaluateMonthList) {
            if (EvaluateUtil.checkHasEvaluateMonth(x)) {
                countMonth++;
            }
        }
        model.setDateStatus(this.getStatus(countDate, evaluateDateList.size()));
        model.setWeekStatus(this.getStatus(countWeek, evaluateWeekList.size()));
        model.setMonthStatus(this.getStatus(countMonth, evaluateMonthList.size()));
        return model;
    }


    private void setWeekReply(Long idSchool, EvaluateWeek x, EvaluateKidTeacherResponse model, Long idUser) {
        List<ReplyTypeEditObject> replyDataList = new ArrayList<>();
        if (StringUtils.isNotBlank(x.getTeacherReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getTeacherReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setType(AppTypeConstant.TEACHER);
            reply.setEditStatus(x.getTeacherReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            reply.setFullName(x.getTeacherReplyCreatedBy());
            reply.setContent(x.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : x.getTeacherReplyContent());
            reply.setCreatedDate(x.getTeacherReplyDatetime());
            reply.setModifyStatus(x.isTeacherReplyModified());
            reply.setRevoke(x.isTeacherReplyDel());
            replyDataList.add(reply);
        }
        if (StringUtils.isNotBlank(x.getSchoolReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getSchoolReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setType(AppTypeConstant.SCHOOL);
            reply.setFullName(x.getSchoolReplyCreatedBy());
            reply.setContent(x.isSchoolReplyDel() ? EvaluateConstant.REVOKE_SCHOOL : x.getSchoolReplyContent());
            reply.setCreatedDate(x.getSchoolReplyDatetime());
            reply.setModifyStatus(x.isSchoolReplyModified());
            reply.setRevoke(x.isSchoolReplyDel());
            replyDataList.add(reply);
        }
        replyDataList = replyDataList.stream().sorted(Comparator.comparing(ReplyTypeEditObject::getCreatedDate)).collect(Collectors.toList());
        if (StringUtils.isNotBlank(x.getParentReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getParentReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarParent(x.getParentReplyIdCreated()));
            reply.setType(AppTypeConstant.PARENT);
            reply.setFullName(x.getParentReplyCreatedBy());
            reply.setContent(x.isParentReplyDel() ? EvaluateConstant.REVOKE_PARENT : x.getParentReplyContent());
            reply.setCreatedDate(x.getParentReplyDatetime());
            reply.setModifyStatus(x.isParentReplyModified());
            reply.setRevoke(x.isParentReplyDel());
            replyDataList.add(0, reply);
        }
        model.setReplyList(replyDataList);
    }

    private void setMonthReply(Long idSchool, EvaluateMonth x, EvaluateKidTeacherResponse model, Long idUser) {
        List<ReplyTypeEditObject> replyDataList = new ArrayList<>();
        if (StringUtils.isNotBlank(x.getTeacherReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getTeacherReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setType(AppTypeConstant.TEACHER);
            reply.setEditStatus(x.getTeacherReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            reply.setFullName(x.getTeacherReplyCreatedBy());
            reply.setContent(x.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : x.getTeacherReplyContent());
            reply.setCreatedDate(x.getTeacherReplyDatetime());
            reply.setModifyStatus(x.isTeacherReplyModified());
            reply.setRevoke(x.isTeacherReplyDel());
            replyDataList.add(reply);
        }
        if (StringUtils.isNotBlank(x.getSchoolReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getSchoolReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setType(AppTypeConstant.SCHOOL);
            reply.setFullName(x.getSchoolReplyCreatedBy());
            reply.setContent(x.isSchoolReplyDel() ? EvaluateConstant.REVOKE_SCHOOL : x.getSchoolReplyContent());
            reply.setCreatedDate(x.getSchoolReplyDatetime());
            reply.setModifyStatus(x.isSchoolReplyModified());
            reply.setRevoke(x.isSchoolReplyDel());
            replyDataList.add(reply);
        }
        replyDataList = replyDataList.stream().sorted(Comparator.comparing(ReplyTypeEditObject::getCreatedDate)).collect(Collectors.toList());
        if (StringUtils.isNotBlank(x.getParentReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getParentReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarParent(x.getParentReplyIdCreated()));
            reply.setType(AppTypeConstant.PARENT);
            reply.setFullName(x.getParentReplyCreatedBy());
            reply.setContent(x.isParentReplyDel() ? EvaluateConstant.REVOKE_PARENT : x.getParentReplyContent());
            reply.setCreatedDate(x.getParentReplyDatetime());
            reply.setModifyStatus(x.isParentReplyModified());
            reply.setRevoke(x.isParentReplyDel());
            replyDataList.add(0, reply);
        }
        model.setReplyList(replyDataList);
    }

    private void setPeriodicReply(Long idSchool, EvaluatePeriodic x, EvaluateKidTeacherResponse model, Long idUser) {
        List<ReplyTypeEditObject> replyDataList = new ArrayList<>();
        if (StringUtils.isNotBlank(x.getTeacherReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getTeacherReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setType(AppTypeConstant.TEACHER);
            reply.setEditStatus(x.getTeacherReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            reply.setFullName(x.getTeacherReplyCreatedBy());
            reply.setContent(x.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : x.getTeacherReplyContent());
            reply.setCreatedDate(x.getTeacherReplyDatetime());
            reply.setModifyStatus(x.isTeacherReplyModified());
            reply.setRevoke(x.isTeacherReplyDel());
            replyDataList.add(reply);
        }
        if (StringUtils.isNotBlank(x.getSchoolReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getSchoolReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setType(AppTypeConstant.SCHOOL);
            reply.setFullName(x.getSchoolReplyCreatedBy());
            reply.setContent(x.isSchoolReplyDel() ? EvaluateConstant.REVOKE_SCHOOL : x.getSchoolReplyContent());
            reply.setCreatedDate(x.getSchoolReplyDatetime());
            reply.setModifyStatus(x.isSchoolReplyModified());
            reply.setRevoke(x.isSchoolReplyDel());
            replyDataList.add(reply);
        }
        replyDataList = replyDataList.stream().sorted(Comparator.comparing(ReplyTypeEditObject::getCreatedDate)).collect(Collectors.toList());
        if (StringUtils.isNotBlank(x.getParentReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(x.getParentReplyIdCreated()).orElseThrow(() -> new NotFoundException("not found MaUser by id"));
            reply.setAvatar(AvatarUtils.getAvatarParent(x.getParentReplyIdCreated()));
            reply.setType(AppTypeConstant.PARENT);
            reply.setFullName(x.getParentReplyCreatedBy());
            reply.setContent(x.isParentReplyDel() ? EvaluateConstant.REVOKE_PARENT : x.getParentReplyContent());
            reply.setCreatedDate(x.getParentReplyDatetime());
            reply.setModifyStatus(x.isParentReplyModified());
            reply.setRevoke(x.isParentReplyDel());
            replyDataList.add(0, reply);
        }
        model.setReplyList(replyDataList);
    }

    private void setProperties(StatisticalOfMonthTeacherResponse model, Long idClass, Long idKid, LocalDate
            startDate, LocalDate endDate) {
        List<Long> idClassList = kidsClassDateRepository.findidClassList(idKid, idClass);
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findEvaluateDateOfMonthKid(idKid, idClass, idClassList, startDate, endDate);
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findEvaluateWeekOfMonthKid(idKid, idClass, idClassList, startDate, endDate);
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findEvaluateMonthOfMontKid(idKid, idClass, idClassList, startDate);
        List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findEvaluatePeriodicOfMontKid(idKid, idClass, idClassList, startDate, endDate);
        model.setDateNumber(evaluateDateList.size());
        model.setWeekNumber(evaluateWeekList.size());
        model.setMonthNumber(evaluateMonthList.size());
        model.setPeriodicNumber(evaluatePeriodicList.size());
        evaluateDateList.forEach(x -> {
            if (StringUtils.isNotBlank(x.getParentReplyContent()) && !x.isParentReplyDel() && !x.isSchoolReadReply()) {
                model.setParentReplyUnread(AppConstant.APP_TRUE);
                return;
            }
        });
        evaluateWeekList.forEach(x -> {
            if (StringUtils.isNotBlank(x.getParentReplyContent()) && !x.isParentReplyDel() && !x.isSchoolReadReply()) {
                model.setParentReplyUnread(AppConstant.APP_TRUE);
                return;
            }
        });
        evaluateMonthList.forEach(x -> {
            if (StringUtils.isNotBlank(x.getParentReplyContent()) && !x.isParentReplyDel() && !x.isSchoolReadReply()) {
                model.setParentReplyUnread(AppConstant.APP_TRUE);
                return;
            }
        });
        evaluatePeriodicList.forEach(x -> {
            if (StringUtils.isNotBlank(x.getParentReplyContent()) && !x.isParentReplyDel() && !x.isSchoolReadReply()) {
                model.setParentReplyUnread(AppConstant.APP_TRUE);
                return;
            }
        });
    }


    /**
     * lưu file cho nhận xét ngày
     *
     * @param multipartFileList
     * @param evaluateDate
     * @param idSchool
     */
    private void saveFileDate(List<MultipartFile> multipartFileList, EvaluateDate evaluateDate, Long idSchool) {
        if (CollectionUtils.isEmpty(multipartFileList)) {
            return;
        }
        RequestValidate.checkMaxfileInput(multipartFileList, UploadDownloadConstant.MAX_FILE);
        multipartFileList.forEach(x -> {
            try {
                EvaluateAttachFile evaluateAttachFile = new EvaluateAttachFile();
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(x, idSchool, UploadDownloadConstant.NHAN_XET);
                evaluateAttachFile.setName(handleFileResponse.getName());
                evaluateAttachFile.setUrl(handleFileResponse.getUrlWeb());
                evaluateAttachFile.setUrlLocal(handleFileResponse.getUrlLocal());
                evaluateAttachFile.setEvaluateDate(evaluateDate);
                evaluateAttachFileRepository.save(evaluateAttachFile);
            } catch (IOException e) {
                logger.warn("Lỗi lưu file trong trong nhận xét");
                e.printStackTrace();
            }
        });
    }

    /**
     * lưu file cho nhận xét tuần
     *
     * @param multipartFileList
     * @param evaluateWeek
     * @param idSchool
     */
    private void saveFileWeek(List<MultipartFile> multipartFileList, EvaluateWeek evaluateWeek, Long idSchool) {
        if (CollectionUtils.isEmpty(multipartFileList)) {
            return;
        }
        RequestValidate.checkMaxfileInput(multipartFileList, UploadDownloadConstant.MAX_FILE);
        multipartFileList.forEach(x -> {
            try {
                EvaluateWeekFile evaluateAttachFile = new EvaluateWeekFile();
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(x, idSchool, UploadDownloadConstant.NHAN_XET);
                evaluateAttachFile.setName(handleFileResponse.getName());
                evaluateAttachFile.setUrl(handleFileResponse.getUrlWeb());
                evaluateAttachFile.setUrlLocal(handleFileResponse.getUrlLocal());
                evaluateAttachFile.setEvaluateWeek(evaluateWeek);
                evaluateWeekFileRepository.save(evaluateAttachFile);
            } catch (IOException e) {
                logger.warn("Lỗi lưu file trong trong nhận xét");
                e.printStackTrace();
            }
        });
    }

    /**
     * lưu file cho nhận xét tuần
     *
     * @param multipartFileList
     * @param evaluateMonth
     * @param idSchool
     */
    private void saveFileMonth(List<MultipartFile> multipartFileList, EvaluateMonth evaluateMonth, Long idSchool) {
        if (CollectionUtils.isEmpty(multipartFileList)) {
            return;
        }
        RequestValidate.checkMaxfileInput(multipartFileList, UploadDownloadConstant.MAX_FILE);
        multipartFileList.forEach(x -> {
            try {
                EvaluateMonthFile evaluateMonthFile = new EvaluateMonthFile();
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(x, idSchool, UploadDownloadConstant.NHAN_XET);
                evaluateMonthFile.setName(handleFileResponse.getName());
                evaluateMonthFile.setUrl(handleFileResponse.getUrlWeb());
                evaluateMonthFile.setUrlLocal(handleFileResponse.getUrlLocal());
                evaluateMonthFile.setEvaluateMonth(evaluateMonth);
                evaluateMonthFileRepository.save(evaluateMonthFile);
            } catch (IOException e) {
                logger.warn("Lỗi lưu file trong trong nhận xét");
                e.printStackTrace();
            }
        });
    }

    /**
     * lưu file cho nhận xét định kỳ
     *
     * @param multipartFileList
     * @param evaluatePeriodic
     * @param idSchool
     */
    private void saveFilePeriodic(List<MultipartFile> multipartFileList, EvaluatePeriodic evaluatePeriodic, Long
            idSchool) {
        if (CollectionUtils.isEmpty(multipartFileList)) {
            return;
        }
        RequestValidate.checkMaxfileInput(multipartFileList, UploadDownloadConstant.MAX_FILE);
        multipartFileList.forEach(x -> {
            try {
                EvaluatePeriodicFile evaluateMonthFile = new EvaluatePeriodicFile();
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(x, idSchool, UploadDownloadConstant.NHAN_XET);
                evaluateMonthFile.setName(handleFileResponse.getName());
                evaluateMonthFile.setUrl(handleFileResponse.getUrlWeb());
                evaluateMonthFile.setUrlLocal(handleFileResponse.getUrlLocal());
                evaluateMonthFile.setEvaluatePeriodic(evaluatePeriodic);
                evaluatePeriodicFileRepository.save(evaluateMonthFile);
            } catch (IOException e) {
                logger.warn("Lỗi lưu file trong trong nhận xét");
                e.printStackTrace();
            }
        });
    }

    /**
     * xóa file
     *
     * @param longList
     */
    private void deleteFileDate(List<Long> longList) {
        if (CollectionUtils.isEmpty(longList)) {
            return;
        }
        longList.forEach(x -> {
            Optional<EvaluateAttachFile> optional = evaluateAttachFileRepository.findById(x);
            if (optional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_FILE_DELETE);
            }
            evaluateAttachFileRepository.deleteByIdCustom(x);
        });
    }

    private void deleteFileWeek(List<Long> longList) {
        if (CollectionUtils.isEmpty(longList)) {
            return;
        }
        longList.forEach(x -> {
            Optional<EvaluateWeekFile> optional = evaluateWeekFileRepository.findById(x);
            if (optional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_FILE_DELETE);
            }
            evaluateWeekFileRepository.deleteByIdCustom(x);
        });
    }

    private void deleteFileMonth(List<Long> longList) {
        if (CollectionUtils.isEmpty(longList)) {
            return;
        }
        longList.forEach(x -> {
            Optional<EvaluateMonthFile> optional = evaluateMonthFileRepository.findById(x);
            if (optional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_FILE_DELETE);
            }
            evaluateMonthFileRepository.deleteByIdCustom(x);
        });
    }

    private void deleteFilePeriodic(List<Long> longList) {
        if (CollectionUtils.isEmpty(longList)) {
            return;
        }
        longList.forEach(x -> {
            Optional<EvaluatePeriodicFile> optional = evaluatePeriodicFileRepository.findById(x);
            if (optional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_FILE_DELETE);
            }
            evaluatePeriodicFileRepository.deleteByIdCustom(x);
        });
    }

    /**
     * check data trước khi tạo nhận xét
     *
     * @param request
     * @return
     */
    private ListEvaluateDateCreateTeacherResponse checkBeforeCreateEvaluateDate
    (ListEvaluateDateCreateTeacherRequest request) {
        ListEvaluateDateCreateTeacherResponse response = new ListEvaluateDateCreateTeacherResponse();
        List<EvaluateDateCreateTeacherResponse> responseList = new ArrayList<>();
        AtomicBoolean checkContent = new AtomicBoolean(false);
        AtomicBoolean checkFile = new AtomicBoolean(false);
        request.getDataList().forEach(x -> {
            LocalDateTime localDateTime = x.getDateTime();
            EvaluateDateCreateTeacherResponse model = new EvaluateDateCreateTeacherResponse();
            EvaluateDate evaluateDateDB = evaluateDateRepository.findById(x.getId()).orElseThrow();
            if (request.getStatus()) {
                if (!x.getLearnContent().equals(evaluateDateDB.getLearnContent())) {
                    if (evaluateDateDB.getLearnDatetime() != null && localDateTime.isBefore(evaluateDateDB.getLearnDatetime())) {
                        model.setLearnContent(evaluateDateDB.getLearnContent());
                        checkContent.set(true);
                    }
                }
                if (!x.getEatContent().equals(evaluateDateDB.getEatContent())) {
                    if (evaluateDateDB.getEatDatetime() != null && localDateTime.isBefore(evaluateDateDB.getEatDatetime())) {
                        model.setEatContent(evaluateDateDB.getEatContent());
                        checkContent.set(true);
                    }
                }
                if (!x.getSleepContent().equals(evaluateDateDB.getSleepContent())) {
                    if (evaluateDateDB.getSleepDatetime() != null && localDateTime.isBefore(evaluateDateDB.getSleepDatetime())) {
                        model.setSleepContent(evaluateDateDB.getSleepContent());
                        checkContent.set(true);
                    }
                }
                if (!x.getSanitaryContent().equals(evaluateDateDB.getSanitaryContent())) {
                    if (evaluateDateDB.getSanitaryDatetime() != null && localDateTime.isBefore(evaluateDateDB.getSanitaryDatetime())) {
                        model.setSanitaryContent(evaluateDateDB.getSanitaryContent());
                        checkContent.set(true);
                    }
                }
                if (!x.getHealtContent().equals(evaluateDateDB.getHealtContent())) {
                    if (evaluateDateDB.getHealtDatetime() != null && localDateTime.isBefore(evaluateDateDB.getHealtDatetime())) {
                        model.setHealtContent(evaluateDateDB.getHealtContent());
                        checkContent.set(true);
                    }
                }
                if (!x.getCommonContent().equals(evaluateDateDB.getCommonContent())) {
                    if (evaluateDateDB.getCommonDatetime() != null && localDateTime.isBefore(evaluateDateDB.getCommonDatetime())) {
                        model.setCommonContent(evaluateDateDB.getCommonContent());
                        checkContent.set(true);
                    }
                }
            }
            //số file truyền vào - số file xóa + số file có sẵn trong DB mà quá 3 thì ko lưu được
            if (!CollectionUtils.isEmpty(x.getMultipartFileList())) {
                int countFileDelete = !CollectionUtils.isEmpty(x.getIdFileDeleteList()) ? x.getIdFileDeleteList().size() : 0;
                int countFileDB = !CollectionUtils.isEmpty(evaluateDateDB.getEvaluateAttachFileList()) ? evaluateDateDB.getEvaluateAttachFileList().size() : 0;
                if (x.getMultipartFileList().size() - countFileDelete + countFileDB > UploadDownloadConstant.MAX_FILE) {
                    checkFile.set(true);
                }
            }
            if (checkContent.get() || checkFile.get()) {
                model.setId(x.getId());
                model.setFileList(listMapper.mapList(evaluateDateDB.getEvaluateAttachFileList(), MobileFileTeacher.class));
                responseList.add(model);
                if (checkContent.get()) {
                    response.setMessage(MessageConstant.EVALUATE_EDIT);
                } else if (checkFile.get()) {
                    response.setMessage(MessageConstant.EVALUATE_MAX_FILE);
                }
            }
        });
        response.setDataList(responseList);
        return response;
    }

    /**
     * check trước khi tạo dữ liệu tuần
     *
     * @param listEvaluateCreateTeacherRequest
     * @return
     */
    private ListEvaluateCreateTeacherResponse checkBeforeCreateWeek(ListEvaluateCreateTeacherRequest
                                                                            listEvaluateCreateTeacherRequest) {
        ListEvaluateCreateTeacherResponse response = new ListEvaluateCreateTeacherResponse();
        List<EvaluateCreateTeacherResponse> responseList = new ArrayList<>();
        AtomicBoolean checkContent = new AtomicBoolean(false);
        AtomicBoolean checkFile = new AtomicBoolean(false);
        listEvaluateCreateTeacherRequest.getDataList().forEach(x -> {
            EvaluateWeek evaluateWeekDB = evaluateWeekRepository.findById(x.getId()).orElseThrow();
            EvaluateCreateTeacherResponse model = new EvaluateCreateTeacherResponse();
            if (listEvaluateCreateTeacherRequest.getStatus()) {
                if (!x.getContent().equals(evaluateWeekDB.getContent()) && evaluateWeekDB.getLastModifieDate() != null && x.getDateTime().isBefore(evaluateWeekDB.getLastModifieDate())) {
                    model.setContent(evaluateWeekDB.getContent());
                    checkContent.set(true);
                }
            }
            if (!CollectionUtils.isEmpty(x.getMultipartFileList())) {
                int countFileDelete = !CollectionUtils.isEmpty(x.getIdFileDeleteList()) ? x.getIdFileDeleteList().size() : 0;
                int countFileDB = !CollectionUtils.isEmpty(evaluateWeekDB.getEvaluateWeekFileList()) ? evaluateWeekDB.getEvaluateWeekFileList().size() : 0;
                if (x.getMultipartFileList().size() - countFileDelete + countFileDB > UploadDownloadConstant.MAX_FILE) {
                    checkFile.set(true);
                }
            }
            if (checkContent.get() || checkFile.get()) {
                model.setId(x.getId());
                model.setFileList(listMapper.mapList(evaluateWeekDB.getEvaluateWeekFileList(), MobileFileTeacher.class));
                responseList.add(model);
                if (checkContent.get()) {
                    response.setMessage(MessageConstant.EVALUATE_EDIT);
                } else if (checkFile.get()) {
                    response.setMessage(MessageConstant.EVALUATE_MAX_FILE);
                }
            }
        });
        response.setDataList(responseList);
        return response;
    }

    /**
     * check trước khi tạo dữ liệu tuần
     *
     * @param listEvaluateCreateTeacherRequest
     * @return
     */
    private ListEvaluateCreateTeacherResponse checkBeforeCreateMonth(ListEvaluateCreateTeacherRequest
                                                                             listEvaluateCreateTeacherRequest) {
        ListEvaluateCreateTeacherResponse response = new ListEvaluateCreateTeacherResponse();
        List<EvaluateCreateTeacherResponse> responseList = new ArrayList<>();
        AtomicBoolean checkContent = new AtomicBoolean(false);
        AtomicBoolean checkFile = new AtomicBoolean(false);
        listEvaluateCreateTeacherRequest.getDataList().forEach(x -> {
            EvaluateMonth evaluateMonthDB = evaluateMonthRepository.findById(x.getId()).orElseThrow();
            EvaluateCreateTeacherResponse model = new EvaluateCreateTeacherResponse();
            //check nội dung khi yêu cầu check là true
            if (listEvaluateCreateTeacherRequest.getStatus()) {
                if (!x.getContent().equals(evaluateMonthDB.getContent()) && evaluateMonthDB.getLastModifieDate() != null && x.getDateTime().isBefore(evaluateMonthDB.getLastModifieDate())) {
                    model.setContent(evaluateMonthDB.getContent());
                    checkContent.set(true);
                }
            }
            //luôn check file
            if (!CollectionUtils.isEmpty(x.getMultipartFileList())) {
                int countFileDelete = !CollectionUtils.isEmpty(x.getIdFileDeleteList()) ? x.getIdFileDeleteList().size() : 0;
                int countFileDB = !CollectionUtils.isEmpty(evaluateMonthDB.getEvaluateMonthFileList()) ? evaluateMonthDB.getEvaluateMonthFileList().size() : 0;
                if (x.getMultipartFileList().size() - countFileDelete + countFileDB > UploadDownloadConstant.MAX_FILE) {
                    checkFile.set(true);
                }
            }
            if (checkContent.get() || checkFile.get()) {
                model.setId(x.getId());
                model.setFileList(listMapper.mapList(evaluateMonthDB.getEvaluateMonthFileList(), MobileFileTeacher.class));
                responseList.add(model);
                if (checkContent.get()) {
                    response.setMessage(MessageConstant.EVALUATE_EDIT);
                } else if (checkFile.get()) {
                    response.setMessage(MessageConstant.EVALUATE_MAX_FILE);
                }
            }
        });
        response.setDataList(responseList);
        return response;
    }

    /**
     * check trước khi tạo nhận xét định kỳ
     *
     * @param listEvaluatePeriodicCreateTeacherRequest
     * @return
     */
    private ListEvaluateCreateTeacherResponse checkBeforeCreatePeriodic(ListEvaluatePeriodicCreateTeacherRequest
                                                                                listEvaluatePeriodicCreateTeacherRequest) {
        ListEvaluateCreateTeacherResponse response = new ListEvaluateCreateTeacherResponse();
        List<EvaluateCreateTeacherResponse> responseList = new ArrayList<>();
        AtomicBoolean checkContent = new AtomicBoolean(false);
        AtomicBoolean checkFile = new AtomicBoolean(false);
        listEvaluatePeriodicCreateTeacherRequest.getDataList().forEach(x -> {
            Long idEvlauatePeriodic = x.getId();
            Optional<EvaluatePeriodic> evaluatePeriodicOptional = evaluatePeriodicRepository.findByDateAndKidsId(LocalDate.now(), x.getIdKid());
            EvaluatePeriodic evaluatePeriodicDB = null;
            //đã có nhận xét định kỳ hoặc có người khác tạo trong lúc lấy về chưa có
            if (idEvlauatePeriodic != null || evaluatePeriodicOptional.isPresent()) {
                if (idEvlauatePeriodic != null) {
                    evaluatePeriodicDB = evaluatePeriodicRepository.findById(idEvlauatePeriodic).orElseThrow();
                } else if (evaluatePeriodicOptional.isPresent()) {
                    evaluatePeriodicDB = evaluatePeriodicOptional.get();
                }
                EvaluateCreateTeacherResponse model = new EvaluateCreateTeacherResponse();
                if (listEvaluatePeriodicCreateTeacherRequest.getStatus()) {
                    if (!x.getContent().equals(evaluatePeriodicDB.getContent()) && evaluatePeriodicDB.getLastModifieDate() != null && x.getDateTime().isBefore(evaluatePeriodicDB.getLastModifieDate())) {
                        model.setContent(evaluatePeriodicDB.getContent());
                        checkContent.set(true);
                    }
                }
                if (!CollectionUtils.isEmpty(x.getMultipartFileList())) {
                    int countFileDelete = !CollectionUtils.isEmpty(x.getIdFileDeleteList()) ? x.getIdFileDeleteList().size() : 0;
                    int countFileDB = !CollectionUtils.isEmpty(evaluatePeriodicDB.getEvaluatePeriodicFileList()) ? evaluatePeriodicDB.getEvaluatePeriodicFileList().size() : 0;
                    if (x.getMultipartFileList().size() - countFileDelete + countFileDB > UploadDownloadConstant.MAX_FILE) {
                        checkFile.set(true);
                    }
                }
                if (checkContent.get() || checkFile.get()) {
                    model.setId(x.getId());
                    model.setFileList(listMapper.mapList(evaluatePeriodicDB.getEvaluatePeriodicFileList(), MobileFileTeacher.class));
                    responseList.add(model);
                    if (checkContent.get()) {
                        response.setMessage(MessageConstant.EVALUATE_EDIT);
                    } else if (checkFile.get()) {
                        response.setMessage(MessageConstant.EVALUATE_MAX_FILE);
                    }
                }
            }
        });
        response.setDataList(responseList);
        return response;
    }

    /**
     * check sửa về rỗng và cho phép chỉnh sửa nhận xét theo config
     *
     * @param evaluateDateDB
     * @param dateRequest
     */
    private void checkBeforeEditDate(EvaluateDate evaluateDateDB, EvaluateDateCreateTeacherRequest dateRequest) {
//        boolean checkChange = this.checkChangeDate(evaluateDateDB, dateRequest);
//        //đã sửa so với DB
//        if (checkChange) {
        //check sửa hết về rỗng
        if (StringUtils.isBlank(dateRequest.getLearnContent()) && StringUtils.isBlank(dateRequest.getEatContent()) && StringUtils.isBlank(dateRequest.getSleepContent()) && StringUtils.isBlank(dateRequest.getSanitaryContent()) && StringUtils.isBlank(dateRequest.getHealtContent()) && StringUtils.isBlank(dateRequest.getCommonContent())
                && CollectionUtils.isEmpty(dateRequest.getMultipartFileList())) {
            if (CollectionUtils.isEmpty(evaluateDateDB.getEvaluateAttachFileList()) || (!CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList()) && dateRequest.getIdFileDeleteList().size() == evaluateDateDB.getEvaluateAttachFileList().size())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_DELETE);
            }
//            }
//            boolean checkHas = EvaluateUtil.checkHasEvaluateDate(evaluateDateDB);
//            //check đã có nhận xét
//            if (checkHas) {
//                //không cho phép sửa theo config
//                if (!editApproved) {
//                    //nhận xét đã duyệt
//                    if (evaluateDateDB.isApproved()) {
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_EDIT_APPROVED);
//                    }
//                }
//            }
        }
    }

    /**
     * check có chỉnh sửa so với DB hay không
     *
     * @param evaluateDate
     * @param dateRequest
     * @return
     */
    private boolean checkChangeDate(EvaluateDate evaluateDate, EvaluateDateCreateTeacherRequest dateRequest) {
        if (!evaluateDate.getLearnContent().equals(dateRequest.getLearnContent()) || !evaluateDate.getEatContent().equals(dateRequest.getEatContent()) ||
                !evaluateDate.getSleepContent().equals(dateRequest.getSleepContent()) || !evaluateDate.getSanitaryContent().equals(dateRequest.getSanitaryContent()) ||
                !evaluateDate.getHealtContent().equals(dateRequest.getHealtContent()) || !evaluateDate.getCommonContent().equals(dateRequest.getCommonContent()) ||
                !CollectionUtils.isEmpty(dateRequest.getMultipartFileList()) || !CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList())) {
            return true;
        }
        return false;
    }

    /**
     * check sửa về rỗng và cho phép chỉnh sửa nhận xét theo config
     *
     * @param evaluateDB
     * @param request
     */
    private void checkBeforeEditWeek(EvaluateWeek evaluateDB, EvaluateCreateTeacherRequest request) {
//        boolean checkChange = this.checkChangeWeek(evaluateDB, request);
//        if (checkChange) {
        //check sửa hết về rỗng
        if (StringUtils.isBlank(request.getContent())
                && CollectionUtils.isEmpty(request.getMultipartFileList())) {
            if (CollectionUtils.isEmpty(evaluateDB.getEvaluateWeekFileList()) || (!CollectionUtils.isEmpty(request.getIdFileDeleteList()) && request.getIdFileDeleteList().size() == evaluateDB.getEvaluateWeekFileList().size())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_DELETE);
            }
//            }
//            boolean checkHas = EvaluateUtil.checkHasEvaluateWeek(evaluateDB);
//            //check đã có nhận xét
//            if (checkHas) {
//                //không cho phép sửa theo config
//                if (!editApproved) {
//                    //nhận xét đã duyệt
//                    if (evaluateDB.isApproved()) {
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_EDIT_APPROVED);
//                    }
//                }
//            }
        }
    }

    private boolean checkChangeWeek(EvaluateWeek evaluate, EvaluateCreateTeacherRequest dateRequest) {
        if (!evaluate.getContent().equals(dateRequest.getContent()) ||
                !CollectionUtils.isEmpty(dateRequest.getMultipartFileList()) || !CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList())) {
            return true;
        }
        return false;
    }


    private boolean checkChangeMonth(EvaluateMonth evaluate, EvaluateCreateTeacherRequest dateRequest) {
        if (!evaluate.getContent().equals(dateRequest.getContent()) ||
                !CollectionUtils.isEmpty(dateRequest.getMultipartFileList()) || !CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList())) {
            return true;
        }
        return false;
    }

    private boolean checkChangePeriodic(EvaluatePeriodic evaluate, EvaluatePeriodicCreateTeacherRequest dateRequest) {
        if (!evaluate.getContent().equals(dateRequest.getContent()) ||
                !CollectionUtils.isEmpty(dateRequest.getMultipartFileList()) || !CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList())) {
            return true;
        }
        return false;
    }


    /**
     * check sửa về rỗng và cho phép chỉnh sửa nhận xét theo config
     *
     * @param evaluateDB
     * @param request
     */
    private void checkBeforeEditMonth(EvaluateMonth evaluateDB, EvaluateCreateTeacherRequest request) {
//        boolean checkChange = this.checkChangeMonth(evaluateDB, request);
//        if (checkChange) {
        //check sửa hết về rỗng
        if (StringUtils.isBlank(request.getContent())
                && CollectionUtils.isEmpty(request.getMultipartFileList())) {
            if (CollectionUtils.isEmpty(evaluateDB.getEvaluateMonthFileList()) || (!CollectionUtils.isEmpty(request.getIdFileDeleteList()) && request.getIdFileDeleteList().size() == evaluateDB.getEvaluateMonthFileList().size())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_DELETE);
            }
//            }
//            boolean checkHas = EvaluateUtil.checkHasEvaluateMonth(evaluateDB);
//            //check đã có nhận xét
//            if (checkHas) {
//                //không cho phép sửa theo config
//                if (!editApproved) {
//                    //nhận xét đã duyệt
//                    if (evaluateDB.isApproved()) {
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_EDIT_APPROVED);
//                    }
//                }
//            }
        }
    }

    /**
     * check sửa về rỗng và cho phép chỉnh sửa nhận xét theo config
     *
     * @param evaluateDB
     * @param request
     */
    private void checkBeforeEditPeriodic(EvaluatePeriodic evaluateDB, EvaluatePeriodicCreateTeacherRequest request) {
        //đầu vào có chỉnh sửa
//        if (!request.getContent().equals(evaluateDB.getContent()) || !CollectionUtils.isEmpty(request.getMultipartFileList()) || !CollectionUtils.isEmpty(request.getIdFileDeleteList())) {
        //check sửa hết data về rỗng và xóa hết file trong db
        if (StringUtils.isBlank(request.getContent())) {
            if (!CollectionUtils.isEmpty(evaluateDB.getEvaluatePeriodicFileList()) && !CollectionUtils.isEmpty(request.getIdFileDeleteList()) && evaluateDB.getEvaluatePeriodicFileList().size() == request.getIdFileDeleteList().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_DELETE);
            }
//            }
//            //đã được nhận xét trong
//            if (StringUtils.isNotBlank(evaluateDB.getContent()) || !CollectionUtils.isEmpty(evaluateDB.getEvaluatePeriodicFileList())) {
//                //không cho phép chỉnh sửa khi đã duyệt
//                if (!editApproved) {
//                    //nhận xét đã duyệt
//                    if (evaluateDB.isApproved()) {
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_EDIT_APPROVED);
//                    }
//                }
//            }
        }
    }

//    /**
//     * check trước khi tạo nhận xét ngày chung
//     *
//     * @param request
//     */
//    private void checkBrforeCreateDateCommon(EvaluateDateCommonCreateTeacherRequest request) {
//        if (StringUtils.isBlank(request.getLearnContent()) && StringUtils.isBlank(request.getEatContent()) && StringUtils.isBlank(request.getSleepContent())
//                && StringUtils.isBlank(request.getSanitaryContent()) && StringUtils.isBlank(request.getHealtContent()) && StringUtils.isBlank(request.getCommonContent()) && CollectionUtils.isEmpty(request.getMultipartFileList())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATA);
//        }
//    }
//
//    /**
//     * check trước khi tạo nhận xét chung
//     *
//     * @param request
//     */
//    private void checkBrforeCreateCommon(EvaluateCommonCreateTeacherRequest request) {
//        if (StringUtils.isBlank(request.getContent()) && CollectionUtils.isEmpty(request.getMultipartFileList())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATA);
//        }
//    }
//
//    private void checkBrforeCreatePeriodicCommon(EvaluatePeriodicCommonCreateTeacherRequest request) {
//        if (StringUtils.isBlank(request.getContent()) && CollectionUtils.isEmpty(request.getMultipartFileList())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATA);
//        }
//    }

    /**
     * set properties for date
     *
     * @param principal
     * @param evaluateDateDB
     * @param request
     */
//    private void saveEvaluateDate(UserPrincipal principal, EvaluateDate evaluateDateDB, EvaluateDateCommonCreateTeacherRequest request) {
//        Long idUser = principal.getId();
//        String fullName = principal.getFullName();
//        LocalDateTime localDateTime = LocalDateTime.now();
//        Long idSchool = principal.getIdSchoolLogin();
//        if (!request.getLearnContent().equals(evaluateDateDB.getLearnContent())) {
//            evaluateDateDB.setLearnContent(request.getLearnContent());
//            evaluateDateDB.setLearnIdCreated(idUser);
//            evaluateDateDB.setLearnCreatedBy(fullName);
//            evaluateDateDB.setLearnDatetime(localDateTime);
//        }
//        if (!request.getEatContent().equals(evaluateDateDB.getEatContent())) {
//            evaluateDateDB.setEatContent(request.getEatContent());
//            evaluateDateDB.setEatIdCreated(idUser);
//            evaluateDateDB.setEatCreatedBy(fullName);
//            evaluateDateDB.setEatDatetime(localDateTime);
//        }
//        if (!request.getSleepContent().equals(evaluateDateDB.getSleepContent())) {
//            evaluateDateDB.setSleepContent(request.getSleepContent());
//            evaluateDateDB.setSleepIdCreated(idUser);
//            evaluateDateDB.setSleepCreatedBy(fullName);
//            evaluateDateDB.setSleepDatetime(localDateTime);
//        }
//        if (!request.getSanitaryContent().equals(evaluateDateDB.getSanitaryContent())) {
//            evaluateDateDB.setSanitaryContent(request.getSanitaryContent());
//            evaluateDateDB.setSanitaryIdCreated(idUser);
//            evaluateDateDB.setSanitaryCreatedBy(fullName);
//            evaluateDateDB.setSanitaryDatetime(localDateTime);
//        }
//        if (!request.getHealtContent().equals(evaluateDateDB.getHealtContent())) {
//            evaluateDateDB.setHealtContent(request.getHealtContent());
//            evaluateDateDB.setHealtIdCreated(idUser);
//            evaluateDateDB.setHealtCreatedBy(fullName);
//            evaluateDateDB.setHealtDatetime(localDateTime);
//        }
//        if (!request.getCommonContent().equals(evaluateDateDB.getCommonContent())) {
//            evaluateDateDB.setCommonContent(request.getCommonContent());
//            evaluateDateDB.setCommonIdCreated(idUser);
//            evaluateDateDB.setCommonCreatedBy(fullName);
//            evaluateDateDB.setCommonDatetime(localDateTime);
//        }
//
//        EvaluateDate evaluateDateSaved = evaluateDateRepository.save(evaluateDateDB);
//        this.deleteFileDate(evaluateDateSaved.getEvaluateAttachFileList().stream().map(x -> x.getId()).collect(Collectors.toList()));
//        this.saveFileDate(request.getMultipartFileList(), evaluateDateSaved, idSchool);
//    }

    /**
     * set properties for week
     *
     * @param principal
     * @param evaluate
     * @param request
     */
//    private void saveEvaluateWeek(UserPrincipal principal, EvaluateWeek evaluate, EvaluateCommonCreateTeacherRequest request) {
//        Long idUser = principal.getId();
//        String fullName = principal.getFullName();
//        LocalDateTime localDateTime = LocalDateTime.now();
//        Long idSchool = principal.getIdSchoolLogin();
//        if (!request.getContent().equals(evaluate.getContent())) {
//            evaluate.setContent(request.getContent());
//            evaluate.setIdModified(idUser);
//            evaluate.setLastModifieBy(fullName);
//            evaluate.setLastModifieDate(localDateTime);
//        }
//        EvaluateWeek evaluateSaved = evaluateWeekRepository.save(evaluate);
//        this.deleteFileWeek(evaluateSaved.getEvaluateWeekFileList().stream().map(x -> x.getId()).collect(Collectors.toList()));
//        this.saveFileWeek(request.getMultipartFileList(), evaluateSaved, idSchool);
//    }

    /**
     * set properties for week
     *
     * @param principal
     * @param evaluate
     * @param request
     */
//    private void saveEvaluateMonth(UserPrincipal principal, EvaluateMonth evaluate, EvaluateCommonCreateTeacherRequest request) {
//        Long idUser = principal.getId();
//        String fullName = principal.getFullName();
//        LocalDateTime localDateTime = LocalDateTime.now();
//        Long idSchool = principal.getIdSchoolLogin();
//        if (!request.getContent().equals(evaluate.getContent())) {
//            evaluate.setContent(request.getContent());
//            evaluate.setIdModified(idUser);
//            evaluate.setLastModifieBy(fullName);
//            evaluate.setLastModifieDate(localDateTime);
//        }
//        EvaluateMonth evaluateSaved = evaluateMonthRepository.save(evaluate);
//        this.deleteFileMonth(evaluateSaved.getEvaluateMonthFileList().stream().map(x -> x.getId()).collect(Collectors.toList()));
//        this.saveFileMonth(request.getMultipartFileList(), evaluateSaved, idSchool);
//    }

//    private void saveEvaluatePeriodic(UserPrincipal principal, EvaluatePeriodic evaluate, EvaluatePeriodicCommonCreateTeacherRequest request) {
//        Long idUser = principal.getId();
//        String fullName = principal.getFullName();
//        LocalDateTime localDateTime = LocalDateTime.now();
//        Long idSchool = principal.getIdSchoolLogin();
//        if (request.getContent().equals(evaluate.getContent())) {
//            evaluate.setContent(request.getContent());
//            evaluate.setIdModified(idUser);
//            evaluate.setLastModifieBy(fullName);
//            evaluate.setLastModifieDate(localDateTime);
//        }
//        EvaluatePeriodic evaluateSaved = evaluatePeriodicRepository.save(evaluate);
//        this.deleteFilePeriodic(evaluateSaved.getEvaluatePeriodicFileList().stream().map(x -> x.getId()).collect(Collectors.toList()));
//        this.saveFilePeriodic(request.getMultipartFileList(), evaluateSaved, idSchool);
//    }

//
//    private void createEvaluatePeriodic(UserPrincipal principal, Long idKid, EvaluatePeriodicCommonCreateTeacherRequest request) {
//        EvaluatePeriodic evaluatePeriodic = new EvaluatePeriodic();
//        Kids kid = kidsRepository.findById(idKid).orElseThrow();
//        Long idUser = principal.getId();
//        String fullName = principal.getFullName();
//        LocalDateTime localDateTime = LocalDateTime.now();
//        evaluatePeriodic.setIdSchool(kid.getIdSchool());
//        evaluatePeriodic.setIdGrade(kid.getIdGrade());
//        evaluatePeriodic.setIdClass(kid.getMaClass().getId());
//        evaluatePeriodic.setDate(LocalDate.now());
//        evaluatePeriodic.setKids(kid);
//        evaluatePeriodic.setApproved(principal.getSchoolConfig().isEvaluate());
//        evaluatePeriodic.setContent(request.getContent());
//        evaluatePeriodic.setIdModified(idUser);
//        evaluatePeriodic.setLastModifieBy(fullName);
//        evaluatePeriodic.setLastModifieDate(localDateTime);
//        EvaluatePeriodic evaluatePeriodicSaved = evaluatePeriodicRepository.save(evaluatePeriodic);
//        this.saveFilePeriodic(request.getMultipartFileList(), evaluatePeriodicSaved, principal.getIdSchoolLogin());
//    }

    /**
     * set model evaluateperiodic
     *
     * @param evaluatePeriodic
     * @return
     */
    private EvaluatePeriodicTeacherResponse setModelEvalutePeridoic(EvaluatePeriodic evaluatePeriodic) {
        EvaluatePeriodicTeacherResponse model = new EvaluatePeriodicTeacherResponse();
        model.setDateTime(LocalDateTime.now());
        model.setId(evaluatePeriodic.getId());
        model.setIdKid(evaluatePeriodic.getKids().getId());
        model.setKidName(evaluatePeriodic.getKids().getFullName());
        model.setAvatar(ConvertData.getAvatarKid(evaluatePeriodic.getKids()));
        model.setStatus(StringUtils.isNotBlank(evaluatePeriodic.getContent()) || !CollectionUtils.isEmpty(evaluatePeriodic.getEvaluatePeriodicFileList()));
        model.setApproved(evaluatePeriodic.isApproved());
        model.setContent(evaluatePeriodic.getContent());
        model.setFileList(listMapper.mapList(evaluatePeriodic.getEvaluatePeriodicFileList(), MobileFileTeacher.class));
        return model;
    }


    private void updateParentRead(EvaluateDate evaluateDate) {
        evaluateDate.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateDateRepository.save(evaluateDate);
    }

    private void setEvaluateDateReply(Long idSchool, EvaluateDate evaluateDate, EvaluateDateKidTeacherResponse model, Long idUser) {
        List<ReplyTypeEditObject> replyDataList = new ArrayList<>();
        //teacher
        if (StringUtils.isNotBlank(evaluateDate.getTeacherReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(evaluateDate.getTeacherReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
            reply.setType(AppTypeConstant.TEACHER);
            reply.setEditStatus(evaluateDate.getTeacherReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setFullName(evaluateDate.getTeacherReplyCreatedBy());
            reply.setContent(evaluateDate.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluateDate.getTeacherReplyContent());
            reply.setCreatedDate(evaluateDate.getTeacherReplyDatetime());
            reply.setModifyStatus(evaluateDate.isTeacherReplyModified());
            reply.setRevoke(evaluateDate.isTeacherReplyDel());
            replyDataList.add(reply);
        }
        //school
        if (StringUtils.isNotBlank(evaluateDate.getSchoolReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(evaluateDate.getSchoolReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of school"));
            reply.setType(AppTypeConstant.SCHOOL);
            reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            reply.setFullName(evaluateDate.getSchoolReplyCreatedBy());
            reply.setContent(evaluateDate.isSchoolReplyDel() ? EvaluateConstant.REVOKE_SCHOOL : evaluateDate.getSchoolReplyContent());
            reply.setCreatedDate(evaluateDate.getSchoolReplyDatetime());
            reply.setModifyStatus(evaluateDate.isSchoolReplyModified());
            reply.setRevoke(evaluateDate.isSchoolReplyDel());
            replyDataList.add(reply);
        }
        replyDataList = replyDataList.stream().sorted(Comparator.comparing(ReplyTypeEditObject::getCreatedDate)).collect(Collectors.toList());
        //parent
        if (StringUtils.isNotBlank(evaluateDate.getParentReplyContent())) {
            ReplyTypeEditObject reply = new ReplyTypeEditObject();
            MaUser maUser = maUserRepository.findById(evaluateDate.getParentReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of parent"));
            reply.setType(AppTypeConstant.PARENT);
            reply.setAvatar(AvatarUtils.getAvatarParent(evaluateDate.getParentReplyIdCreated()));
            reply.setFullName(evaluateDate.getParentReplyCreatedBy());
            reply.setContent(evaluateDate.isParentReplyDel() ? EvaluateConstant.REVOKE_PARENT : evaluateDate.getParentReplyContent());
            reply.setCreatedDate(evaluateDate.getParentReplyDatetime());
            reply.setModifyStatus(evaluateDate.isParentReplyModified());
            reply.setRevoke(evaluateDate.isParentReplyDel());
            replyDataList.add(0, reply);
        }
        model.setReplyList(replyDataList);
    }

    private void setDateReplyData(UserPrincipal principal, EvaluateDate evaluateDate, ContentAndIdMobileRequest
            request) {
        evaluateDate.setTeacherReplyModified(evaluateDate.getTeacherReplyIdCreated() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        evaluateDate.setTeacherReplyContent(request.getContent());
        evaluateDate.setTeacherReplyIdCreated(principal.getId());
        evaluateDate.setTeacherReplyCreatedBy(principal.getFullName());
        evaluateDate.setTeacherReplyDatetime(LocalDateTime.now());
        evaluateDate.setTeacherReplyDel(AppConstant.APP_FALSE);
        evaluateDate.setParentRead(AppConstant.APP_FALSE);
    }

    private void setWeekReplyData(UserPrincipal principal, EvaluateWeek evaluate, ContentAndIdMobileRequest request) {
        evaluate.setTeacherReplyModified(evaluate.getTeacherReplyIdCreated() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        evaluate.setTeacherReplyContent(request.getContent());
        evaluate.setTeacherReplyIdCreated(principal.getId());
        evaluate.setTeacherReplyCreatedBy(principal.getFullName());
        evaluate.setTeacherReplyDatetime(LocalDateTime.now());
        evaluate.setTeacherReplyDel(AppConstant.APP_FALSE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
    }

    private void setMonthReplyData(UserPrincipal principal, EvaluateMonth evaluate, ContentAndIdMobileRequest
            request) {
        evaluate.setTeacherReplyModified(evaluate.getTeacherReplyIdCreated() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        evaluate.setTeacherReplyContent(request.getContent());
        evaluate.setTeacherReplyIdCreated(principal.getId());
        evaluate.setTeacherReplyCreatedBy(principal.getFullName());
        evaluate.setTeacherReplyDatetime(LocalDateTime.now());
        evaluate.setTeacherReplyDel(AppConstant.APP_FALSE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
    }

    private void setPeriodicReplyData(UserPrincipal principal, EvaluatePeriodic evaluate, ContentAndIdMobileRequest
            request) {
        evaluate.setTeacherReplyModified(evaluate.getTeacherReplyIdCreated() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        evaluate.setTeacherReplyContent(request.getContent());
        evaluate.setTeacherReplyIdCreated(principal.getId());
        evaluate.setTeacherReplyCreatedBy(principal.getFullName());
        evaluate.setTeacherReplyDatetime(LocalDateTime.now());
        evaluate.setTeacherReplyDel(AppConstant.APP_FALSE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
    }

    private ReplyTypeEditObject setDateReplyResponse(Long idSchool, EvaluateDate evaluate, Long idUser) {
        ReplyTypeEditObject reply = new ReplyTypeEditObject();
        MaUser maUser = maUserRepository.findById(evaluate.getTeacherReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
        reply.setType(AppTypeConstant.TEACHER);
        reply.setEditStatus(evaluate.getTeacherReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        reply.setFullName(evaluate.getTeacherReplyCreatedBy());
        reply.setContent(evaluate.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluate.getTeacherReplyContent());
        reply.setCreatedDate(evaluate.getTeacherReplyDatetime());
        reply.setModifyStatus(evaluate.isTeacherReplyModified());
        reply.setRevoke(evaluate.isTeacherReplyDel());
        return reply;
    }

    private ReplyTypeEditObject setWeekReplyResponse(Long idSchool, EvaluateWeek evaluate, Long idUser) {
        ReplyTypeEditObject reply = new ReplyTypeEditObject();
        MaUser maUser = maUserRepository.findById(evaluate.getTeacherReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
        reply.setType(AppTypeConstant.TEACHER);
        reply.setEditStatus(evaluate.getTeacherReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        reply.setFullName(evaluate.getTeacherReplyCreatedBy());
        reply.setContent(evaluate.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluate.getTeacherReplyContent());
        reply.setCreatedDate(evaluate.getTeacherReplyDatetime());
        reply.setModifyStatus(evaluate.isTeacherReplyModified());
        reply.setRevoke(evaluate.isTeacherReplyDel());
        return reply;
    }

    private ReplyTypeEditObject setMonthReplyResponse(Long idSchool, EvaluateMonth evaluate, Long idUser) {
        ReplyTypeEditObject reply = new ReplyTypeEditObject();
        MaUser maUser = maUserRepository.findById(evaluate.getTeacherReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
        reply.setType(AppTypeConstant.TEACHER);
        reply.setEditStatus(evaluate.getTeacherReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        reply.setFullName(evaluate.getTeacherReplyCreatedBy());
        reply.setContent(evaluate.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluate.getTeacherReplyContent());
        reply.setCreatedDate(evaluate.getTeacherReplyDatetime());
        reply.setModifyStatus(evaluate.isTeacherReplyModified());
        reply.setRevoke(evaluate.isTeacherReplyDel());
        return reply;
    }

    private ReplyTypeEditObject setPeriodicReplyResponse(Long idSchool, EvaluatePeriodic evaluate, Long idUser) {
        ReplyTypeEditObject reply = new ReplyTypeEditObject();
        MaUser maUser = maUserRepository.findById(evaluate.getTeacherReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
        reply.setType(AppTypeConstant.TEACHER);
        reply.setEditStatus(evaluate.getTeacherReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        reply.setFullName(evaluate.getTeacherReplyCreatedBy());
        reply.setContent(evaluate.isTeacherReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluate.getTeacherReplyContent());
        reply.setCreatedDate(evaluate.getTeacherReplyDatetime());
        reply.setModifyStatus(evaluate.isTeacherReplyModified());
        reply.setRevoke(evaluate.isTeacherReplyDel());
        return reply;
    }

    private String getStatus(int count, int size) {
        if (count == 0) {
            return AppConstant.EMPTY;
        } else if (count == size) {
            return AppConstant.FULL;
        } else {
            return AppConstant.PART;
        }
    }

    private String getKidsStatus(Kids kids, Long idClassLogin) {
        return idClassLogin.equals(kids.getMaClass().getId()) ? kids.getKidStatus() : AppConstant.OTHER_CLASS;
    }
}
