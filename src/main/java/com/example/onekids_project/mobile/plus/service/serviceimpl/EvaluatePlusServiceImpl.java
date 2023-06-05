package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.enums.StudentStatusEnum;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.KidsPageNumberPlusRequest;
import com.example.onekids_project.mobile.plus.request.KidsSearchPlusRequest;
import com.example.onekids_project.mobile.plus.request.evaluate.*;
import com.example.onekids_project.mobile.plus.response.evaluate.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.EvaluatePlusService;
import com.example.onekids_project.mobile.request.ContentAndIdMobileRequest;
import com.example.onekids_project.mobile.response.*;
import com.example.onekids_project.mobile.teacher.response.evaluate.DateStatusObject;
import com.example.onekids_project.mobile.teacher.response.evaluate.EvaluateStatusResponse;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.evaluatekids.EvaluateClassDateRequest;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
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
public class EvaluatePlusServiceImpl implements EvaluatePlusService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KidsClassDateRepository kidsClassDateRepository;

    @Autowired
    private EvaluateDateRepository evaluateDateRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private ListMapper listMapper;

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
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public List<EvaluateDatePlusResponse> findEvaluateKisDate(UserPrincipal principal, EvaluateClassDateRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findEvaluateDateTeacher(request.getIdClass(), request.getDate());
        List<EvaluateDatePlusResponse> responses = new ArrayList<>();
        evaluateDateList.forEach(x -> {
            AttendanceKids attendanceKids = attendanceKidsRepository.findByAttendanceDateAndKidsId(request.getDate(), x.getKids().getId()).orElseThrow(() -> new NotFoundException("not found attendanceKids by id in teacher"));
            EvaluateDatePlusResponse model = new EvaluateDatePlusResponse();
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
            responses.add(model);
        });
        return responses;
    }

    @Override
    public List<Integer> findEvaluateDateOfMonth(UserPrincipal principal, EvaluateClassDateRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<Integer> integerList = new ArrayList<>();
        LocalDate startDate = LocalDate.of(request.getDate().getYear(), request.getDate().getMonthValue(), 1);
        LocalDate endDate = startDate.plusMonths(1);
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findEvaluateDateOfMonthTeacher(request.getIdClass(), startDate, endDate);
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
        } while (count <= endDate.minusDays(1).getDayOfMonth());
        return integerList;
    }

    @Transactional
    @Override
    public ListEvaluateDateCreatePlusResponse createEvaluateDate(UserPrincipal principal, ListEvaluateDateCreatePlusRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        ListEvaluateDateCreatePlusResponse response = this.checkBeforeCreateEvaluateDate(request);
        if (CollectionUtils.isEmpty(response.getDataList())) {
            Long idUser = principal.getId();
            String fullName = principal.getFullName();
            LocalDateTime localDateTime = LocalDateTime.now();
            Long idSchool = principal.getIdSchoolLogin();
            List<EvaluateDate> evaluateDateList = new ArrayList<>();
            int failNumber = 0;
            for (EvaluateDateCreatePlusRequest x : request.getDataList()) {
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
                                firebaseFunctionService.sendParentByPlusNoContent(38L, evaluateDateDB.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
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
            response.setFailNumber(failNumber);
        }
        return response;
    }

    @Override
    public List<EvaluatePlusResponse> findEvaluateKidsWeek(UserPrincipal principal, EvaluateClassDateRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findByEvaluateWeekWidthClass(request.getIdClass(), request.getDate());
        List<EvaluatePlusResponse> responseList = new ArrayList<>();
        evaluateWeekList.forEach(x -> {
            EvaluatePlusResponse model = new EvaluatePlusResponse();
            model.setId(x.getId());
            model.setKidsStatus(this.getKidsStatus(x.getKids(), request.getIdClass()));
            model.setStatus(StringUtils.isNotBlank(x.getContent()) || !CollectionUtils.isEmpty(x.getEvaluateWeekFileList()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            model.setKidName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setApproved(x.isApproved());
            model.setContent(x.getContent());
            model.setFileList(listMapper.mapList(x.getEvaluateWeekFileList(), MobileFilePlus.class));
            responseList.add(model);
        });
        return responseList;
    }

    @Transactional
    @Override
    public ListEvaluateCreatePlusResponse createEvaluateWeek(UserPrincipal principal, ListEvaluateCreatePlusRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        ListEvaluateCreatePlusResponse response = this.checkBeforeCreateWeek(request);
        if (CollectionUtils.isEmpty(response.getDataList())) {
            Long idUser = principal.getId();
            String fullName = principal.getFullName();
            LocalDateTime localDateTime = LocalDateTime.now();
            Long idSchool = principal.getIdSchoolLogin();
            List<EvaluateWeek> evaluateWeeksList = new ArrayList<>();
            int failNumber = 0;
            for (EvaluateCreatePlusRequest x : request.getDataList()) {
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
                                firebaseFunctionService.sendParentByPlusNoContent(42L, evaluateWeek.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
                            }
                        }
                        EvaluateWeek evaluateWeekSaved = evaluateWeekRepository.save(evaluateWeek);
                        this.saveFileWeek(x.getMultipartFileList(), evaluateWeekSaved, idSchool);
                        this.deleteFileWeek(x.getIdFileDeleteList());
                        evaluateWeeksList.add(evaluateWeek);
                    }
                }
            }
            response.setFailNumber(failNumber);
        }
        return response;
    }

    @Override
    public List<EvaluatePlusResponse> findEvaluateKidsMonth(UserPrincipal principal, EvaluateClassDateRequest request) {
        CommonValidate.checkDataPlus(principal);
        int month = request.getDate().getMonthValue();
        int year = request.getDate().getYear();
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findByEvaluateMonthWithClass(request.getIdClass(), month, year);
        List<EvaluatePlusResponse> responseList = new ArrayList<>();
        evaluateMonthList.forEach(x -> {
            EvaluatePlusResponse model = new EvaluatePlusResponse();
            model.setId(x.getId());
            model.setKidsStatus(this.getKidsStatus(x.getKids(), request.getIdClass()));
            model.setStatus(StringUtils.isNotBlank(x.getContent()) || !CollectionUtils.isEmpty(x.getEvaluateMonthFileList()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
            model.setKidName(x.getKids().getFullName());
            model.setAvatar(ConvertData.getAvatarKid(x.getKids()));
            model.setApproved(x.isApproved());
            model.setContent(x.getContent());
            model.setFileList(listMapper.mapList(x.getEvaluateMonthFileList(), MobileFilePlus.class));
            responseList.add(model);
        });
        return responseList;
    }

    @Transactional
    @Override
    public ListEvaluateCreatePlusResponse createEvaluateMonth(UserPrincipal principal, ListEvaluateCreatePlusRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        ListEvaluateCreatePlusResponse response = this.checkBeforeCreateMonth(request);
        if (CollectionUtils.isEmpty(response.getDataList())) {
            Long idUser = principal.getId();
            String fullName = principal.getFullName();
            LocalDateTime localDateTime = LocalDateTime.now();
            Long idSchool = principal.getIdSchoolLogin();
            List<EvaluateMonth> evaluateMonthList = new ArrayList<>();
            int failNumber = 0;
            for (EvaluateCreatePlusRequest x : request.getDataList()) {
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
                                firebaseFunctionService.sendParentByPlusNoContent(46L, evaluateMonth.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
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
            response.setFailNumber(failNumber);
        }
        return response;
    }

    @Override
    public List<EvaluatePeriodicPlusResponse> findEvaluateKidsPeriodic(UserPrincipal principal, EvaluateClassDateRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idClass = request.getIdClass();
        List<EvaluatePeriodicPlusResponse> responseList = new ArrayList<>();
        LocalDate nowDate = LocalDate.now();
        //chọn ngày quá khứ
        if (nowDate.isAfter(request.getDate())) {
            List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findByEvaluatePeriodicWithClass(idClass, request.getDate());
            evaluatePeriodicList.forEach(x -> {
                EvaluatePeriodicPlusResponse model = this.setModelEvalutePeridoic(x);
                responseList.add(model);
            });
        } else if (nowDate.isEqual(request.getDate())) {
            //ngày hiện tại
            List<Kids> kidsList = kidsRepository.findByKidsClassWithStatus(idClass, StudentStatusEnum.STUDYING.toString());
            kidsList.forEach(x -> {
                EvaluatePeriodicPlusResponse model;
                Optional<EvaluatePeriodic> evaluatePeriodicOptional = evaluatePeriodicRepository.findByDateAndKidsId(LocalDate.now(), x.getId());
                //đã có nhận xét định kì
                if (evaluatePeriodicOptional.isPresent()) {
                    model = this.setModelEvalutePeridoic(evaluatePeriodicOptional.get());
                } else {
                    //chưa có nhận xét định kỳ
                    model = new EvaluatePeriodicPlusResponse();
                    model.setDateTime(LocalDateTime.now());
                    model.setIdKid(x.getId());
                    model.setKidName(x.getFullName());
                    model.setAvatar(ConvertData.getAvatarKid(x));
                    model.setContent("");
                    model.setFileList(new ArrayList<>());
                }
                responseList.add(model);
            });
        }
        return responseList;
    }

    @Transactional
    @Override
    public ListEvaluateCreatePlusResponse createEvaluatePeriodic(UserPrincipal principal, ListEvaluatePeriodicCreatePlusRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        ListEvaluateCreatePlusResponse response = this.checkBeforeCreatePeriodic(request);
        if (CollectionUtils.isEmpty(response.getDataList())) {
            Long idUser = principal.getId();
            String fullName = principal.getFullName();
            LocalDateTime localDateTime = LocalDateTime.now();
            Long idSchool = principal.getIdSchoolLogin();
            List<EvaluatePeriodic> evaluatePeriodicList = new ArrayList<>();
            LocalDate nowDate = LocalDate.now();
            int failNumber = 0;
            for (EvaluatePeriodicCreatePlusRequest x : request.getDataList()) {
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
                            firebaseFunctionService.sendParentByPlusNoContent(50L, evaluatePeriodic.getKids(), FirebaseConstant.CATEGORY_EVALUATE);
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
            response.setFailNumber(failNumber);
        }
        return response;
    }

    @Override
    public List<Integer> statisticalPeriodicOfMonthDate(UserPrincipal principal, EvaluateClassDateRequest request) {
        CommonValidate.checkDataPlus(principal);
        int dayNumberOfMonth = ConvertData.getDateNumberOfMonth(request.getDate());
        Long idClass = request.getIdClass();
        List<Integer> integerList = new ArrayList<>();
        for (int i = 1; i <= dayNumberOfMonth; i++) {
            LocalDate date = LocalDate.of(request.getDate().getYear(), request.getDate().getMonthValue(), i);
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
    public List<StatisticalOfMonthPlusResponse> statisticalOfMonth(UserPrincipal principal, EvaluateClassDateRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate startDate = LocalDate.of(request.getDate().getYear(), request.getDate().getMonthValue(), 1);
        LocalDate endDate = startDate.plusMonths(1);
        Long idClass = request.getIdClass();
        List<Long> idKidDateList = evaluateDateRepository.findIdKidOfMonthList(request.getIdClass(), startDate, endDate);
        List<Long> idKidMonthList = evaluateMonthRepository.findIdKidOfMonthList(idClass, request.getDate().getMonthValue(), request.getDate().getYear());
        List<Long> idKidList = ConvertData.intersecionList(idKidDateList, idKidMonthList);
        List<Kids> kidsList = kidsRepository.findKidsByIdList(idKidList);
        List<StatisticalOfMonthPlusResponse> responseList = new ArrayList<>();
        kidsList.forEach(kids -> {
            StatisticalOfMonthPlusResponse model = new StatisticalOfMonthPlusResponse();
            model.setIdKid(kids.getId());
            model.setKidName(kids.getFullName());
            model.setKidsStatus(this.getKidsStatus(kids, idClass));
            model.setAvatar(ConvertData.getAvatarKid(kids));
            this.setProperties(model, idClass, kids.getId(), startDate, endDate);
            responseList.add(model);
        });
        return responseList;
    }


    @Override
    public List<Integer> statisticalDateAndPeriodic(UserPrincipal principal, EvaluateClassDateRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<Integer> responseList = new ArrayList<>();
        Long idClass = request.getIdClass();
        int daysInMonth = ConvertData.getDateNumberOfMonth(request.getDate());
        for (int i = 1; i <= daysInMonth; i++) {
            LocalDate date = LocalDate.of(request.getDate().getYear(), request.getDate().getMonthValue(), i);
            if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())) {
                List<EvaluateDate> evaluateDateList = evaluateDateRepository.findEvaluateClassDate(idClass, date);
                List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findByIdClassAndDate(idClass, date);
                if (!CollectionUtils.isEmpty(evaluateDateList) || !CollectionUtils.isEmpty(evaluatePeriodicList)) {
                    responseList.add(i);
                }
            }
        }
        return responseList;
    }

    @Override
    public TotalPlusResponse findSchoolUnread(UserPrincipal principal, KidsSearchPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        TotalPlusResponse model = new TotalPlusResponse();
        Long idSchool = principal.getIdSchoolLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        Long idClass = kids.getMaClass().getId();
        Long idKid = kids.getId();
        LocalDate localDate = request.getDate();
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
    public EvaluateDateKidPlusResponse getEvaluateDateKid(UserPrincipal principal, KidsSearchPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        EvaluateDateKidPlusResponse response = new EvaluateDateKidPlusResponse();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        Long idClass = kids.getMaClass().getId();
        Long idKid = kids.getId();
        LocalDate localDate = request.getDate();
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
            response.setId(evaluateDate.getId());
            response.setDate(evaluateDate.getDate());
            response.setLearnContent(evaluateDate.getLearnContent());
            response.setEatContent(evaluateDate.getEatContent());
            response.setApproved(evaluateDate.isApproved());
            response.setSleepContent(evaluateDate.getSleepContent());
            response.setSanitaryContent(evaluateDate.getSanitaryContent());
            response.setHealtContent(evaluateDate.getHealtContent());
            response.setCommonContent(evaluateDate.getCommonContent());
            response.setFileList(listMapper.mapList(evaluateDate.getEvaluateAttachFileList(), AttachFileMobileResponse.class));
            this.setEvaluateDateReply(idSchool, evaluateDate, response, principal.getId());
            this.updateParentRead(evaluateDate);
        }
        return response;
    }

    @Override
    public int countKidDateSchoolUnread(UserPrincipal principal, KidsSearchPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        Long idClass = kids.getMaClass().getId();
        Long idKid = kids.getId();
        LocalDate localDate = request.getDate();
        StartEndDateObject startEndDateObject = ConvertData.getStartEndDateOfMonth(localDate);
        int count = evaluateDateRepository.countSchoolUnreadKidOfMonth(idClass, idKid, startEndDateObject);
        return count;
    }

    @Override
    public EvaluateDateHavePlusResponse findKidDateHaveOfMonth(UserPrincipal principal, KidsSearchPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        EvaluateDateHavePlusResponse response = new EvaluateDateHavePlusResponse();
        List<DateStatusObject> evaluateMonthList = new ArrayList<>();
        List<DateStatusObject> evaluateReplyMonthList = new ArrayList<>();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        Long idClass = null;
        Long idKid = kids.getId();
        LocalDate localDate = request.getDate();
        StartEndDateObject startEndDateObject = ConvertData.getStartEndDateOfMonth(localDate);
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findKidDateHaveOfMonth(idClass, idKid, startEndDateObject);
        evaluateDateList.forEach(x -> {
            int date = x.getDate().getDayOfMonth();
            DateStatusObject model = new DateStatusObject();
            model.setDate(date);
            model.setStatus(x.isApproved());
            evaluateMonthList.add(model);
        });
        List<EvaluateDate> evaluateDateReplyList = evaluateDateRepository.findKidDateHaveOfReplyMonth(idClass, idKid, startEndDateObject);
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
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateDate evaluateDate = evaluateDateRepository.findById(request.getId()).orElseThrow();
        boolean checkSendFirebase = evaluateDate.isApproved() && evaluateDate.getSchoolReplyIdCreated() == null;
        this.setDateReplyData(principal, evaluateDate, request);
        EvaluateDate evaluateDateSaved = evaluateDateRepository.save(evaluateDate);
        ReplyTypeEditObject response = this.setDateReplyResponse(idSchool, evaluateDateSaved, principal.getId());
        if (checkSendFirebase) {
            firebaseFunctionService.sendParentByPlus(41L, evaluateDate.getKids(), FirebaseConstant.CATEGORY_EVALUATE, request.getContent());
        }
        return response;
    }

    @Override
    public ReplyTypeEditObject revokeKidDateReplye(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateDate evaluateDate = evaluateDateRepository.findById(id).orElseThrow();
        evaluateDate.setSchoolReplyDel(AppConstant.APP_TRUE);
        evaluateDate.setParentRead(AppConstant.APP_FALSE);
        EvaluateDate evaluateDateSaved = evaluateDateRepository.save(evaluateDate);
        ReplyTypeEditObject model = this.setDateReplyResponse(idSchool, evaluateDateSaved, principal.getId());
        return model;
    }

    @Override
    public ListEvaluateKidPlusResponse getEvaluateWeekKid(UserPrincipal principal, KidsPageNumberPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        ListEvaluateKidPlusResponse response = new ListEvaluateKidPlusResponse();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        Long idClass = kids.getMaClass().getId();
        Long idSchool = principal.getIdSchoolLogin();
        List<Long> idClassList = kidsClassDateRepository.findidClassList(request.getIdKid(), idClass);
        //todo historyView đang sai, trường cũ cho xem hay không chứ phải ko phải trường mới cho xem hay không
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findEvaluateWeekKidAndPaging(principal.getIdSchoolLogin(), idClass, idClassList, principal.getSchoolConfig().isHistoryViewParent(), request);
        List<EvaluateKidPlusResponse> dataList = new ArrayList<>();
        evaluateWeekList.forEach(x -> {
            EvaluateKidPlusResponse model = new EvaluateKidPlusResponse();
            model.setId(x.getId());
            model.setName(ConvertData.convertDateToWeek(x.getWeek(), x.getDate()));
            model.setApproved(x.isApproved());
            model.setContent(x.getContent());
            model.setReadStatus(!x.isSchoolReadReply() && StringUtils.isNotBlank(x.getParentReplyContent()));
            model.setSameClass(x.getIdClass().equals(idClass));
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
        CommonValidate.checkDataPlus(principal);
        EvaluateWeek evaluateWeek = evaluateWeekRepository.findById(id).orElseThrow();
        evaluateWeek.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateWeekRepository.save(evaluateWeek);
        return true;
    }

    @Transactional
    @Override
    public ReplyTypeEditObject createKidWeekReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateWeek evaluate = evaluateWeekRepository.findById(request.getId()).orElseThrow();
        boolean checkSendFirebase = evaluate.isApproved() && evaluate.getSchoolReplyIdCreated() == null;
        this.setWeekReplyData(principal, evaluate, request);
        EvaluateWeek evaluateDateSaved = evaluateWeekRepository.save(evaluate);
        ReplyTypeEditObject response = this.setWeekReplyResponse(idSchool, evaluateDateSaved, principal.getId());
        if (checkSendFirebase) {
            firebaseFunctionService.sendParentByPlus(45L, evaluate.getKids(), FirebaseConstant.CATEGORY_EVALUATE, request.getContent());
        }
        return response;
    }


    @Override
    public ReplyTypeEditObject revokeKidWeekReplye(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateWeek evaluate = evaluateWeekRepository.findById(id).orElseThrow();
        evaluate.setSchoolReplyDel(AppConstant.APP_TRUE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
        EvaluateWeek evaluateSaved = evaluateWeekRepository.save(evaluate);
        ReplyTypeEditObject response = this.setWeekReplyResponse(idSchool, evaluateSaved, principal.getId());
        return response;
    }

    @Override
    public ListEvaluateKidPlusResponse getEvaluateMonthKid(UserPrincipal principal, KidsPageNumberPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        Long idClassLogin = kids.getMaClass().getId();
        ListEvaluateKidPlusResponse response = new ListEvaluateKidPlusResponse();
        List<Long> idClassList = kidsClassDateRepository.findidClassList(request.getIdKid(), idClassLogin);
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findEvaluateMonthKidAndPaging(principal.getIdSchoolLogin(), idClassLogin, idClassList, principal.getSchoolConfig().isHistoryViewParent(), request);
        List<EvaluateKidPlusResponse> dataList = new ArrayList<>();
        evaluateMonthList.forEach(x -> {
            EvaluateKidPlusResponse model = new EvaluateKidPlusResponse();
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
        CommonValidate.checkDataPlus(principal);
        EvaluateMonth evaluateMonth = evaluateMonthRepository.findById(id).orElseThrow();
        evaluateMonth.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateMonthRepository.save(evaluateMonth);
        return true;
    }

    @Transactional
    @Override
    public ReplyTypeEditObject createKidMonthReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateMonth evaluate = evaluateMonthRepository.findById(request.getId()).orElseThrow();
        boolean checkSendFirebase = evaluate.isApproved() && evaluate.getSchoolReplyIdCreated() == null;
        this.setMonthReplyData(principal, evaluate, request);
        EvaluateMonth evaluateMonthSaved = evaluateMonthRepository.save(evaluate);
        ReplyTypeEditObject response = this.setMonthReplyResponse(idSchool, evaluateMonthSaved, principal.getId());
        if (checkSendFirebase) {
            firebaseFunctionService.sendParentByPlus(49L, evaluate.getKids(), FirebaseConstant.CATEGORY_EVALUATE, request.getContent());
        }
        return response;
    }

    @Override
    public ReplyTypeEditObject revokeKidMonthReply(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluateMonth evaluate = evaluateMonthRepository.findById(id).orElseThrow();
        evaluate.setSchoolReplyDel(AppConstant.APP_TRUE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
        EvaluateMonth evaluateSaved = evaluateMonthRepository.save(evaluate);
        ReplyTypeEditObject response = this.setMonthReplyResponse(idSchool, evaluateSaved, principal.getId());
        return response;
    }

    @Override
    public boolean viewEvaluatePeriodicKid(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findById(id).orElseThrow();
        evaluatePeriodic.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluatePeriodicRepository.save(evaluatePeriodic);
        return true;
    }

    @Transactional
    @Override
    public ReplyTypeEditObject createKidPeriodicReply(UserPrincipal principal, ContentAndIdMobileRequest request) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluatePeriodic evaluate = evaluatePeriodicRepository.findById(request.getId()).orElseThrow();
        boolean checkSendFirebase = evaluate.isApproved() && evaluate.getSchoolReplyIdCreated() == null;
        this.setPeriodicReplyData(principal, evaluate, request);
        EvaluatePeriodic evaluateSaved = evaluatePeriodicRepository.save(evaluate);
        if (checkSendFirebase) {
            firebaseFunctionService.sendParentByPlus(53L, evaluate.getKids(), FirebaseConstant.CATEGORY_EVALUATE, request.getContent());
        }
        ReplyTypeEditObject response = this.setPeriodicReplyResponse(idSchool, evaluateSaved, principal.getId());
        return response;
    }

    @Override
    public ReplyTypeEditObject revokeKidPeriodicReply(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        EvaluatePeriodic evaluate = evaluatePeriodicRepository.findById(id).orElseThrow();
        evaluate.setSchoolReplyDel(AppConstant.APP_TRUE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
        EvaluatePeriodic evaluateSaved = evaluatePeriodicRepository.save(evaluate);
        ReplyTypeEditObject response = this.setPeriodicReplyResponse(idSchool, evaluateSaved, principal.getId());
        return response;
    }

    @Override
    public ListEvaluateKidPlusResponse getEvaluatePeriodicKid(UserPrincipal principal, KidsPageNumberPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        Long idClassLogin = kids.getMaClass().getId();
        ListEvaluateKidPlusResponse response = new ListEvaluateKidPlusResponse();
        List<Long> idClassList = kidsClassDateRepository.findidClassList(request.getIdKid(), idClassLogin);
        List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findEvaluatePeriodicKidAndPaging(principal.getIdSchoolLogin(), idClassLogin, idClassList, principal.getSchoolConfig().isHistoryViewParent(), request);
        List<EvaluateKidPlusResponse> dataList = new ArrayList<>();
        evaluatePeriodicList.forEach(x -> {
            EvaluateKidPlusResponse model = new EvaluateKidPlusResponse();
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
    public List<EvaluateStatusKidDayPlusResponse> findEvaluateStatusKidDate(UserPrincipal principal, EvaluatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<EvaluateStatusKidDayPlusResponse> responseList = new ArrayList<>();
        List<MaClass> maClassList = maClassRepository.findClassInGradeWithDate(principal.getIdSchoolLogin(), request.getId(), request.getDate());
        maClassList.forEach(x -> {
            EvaluateStatusKidDayPlusResponse response = new EvaluateStatusKidDayPlusResponse();
            List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(request.getDate(), x.getId());
//            List<Kids> kidsList = kidsRepository.findKidsByIdList(StudentUtil.getKidsClassList(request.getDate(), x.getId()));
//            List<Kids> kidsList = kidsRepository.findKidOneClassAndStatusWithDate(request.getDate(), x.getId());
//            List<Kids> kidsList1 = attendanceKidsRepository.findByDelActiveTrueAndMaClass_IdAndAttendanceDate(x.getId(), request.getDate()).stream().map(AttendanceKids::getKids).collect(Collectors.toList());

            List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByKidsInAndAttendanceDate(kidsList, request.getDate());
            int absent = (int) attendanceKidsList.stream().filter(AttendanceKids::isAbsentStatus).count();
            List<EvaluateDate> evaluateDateList = evaluateDateRepository.findByIdClassAndDate(x.getId(), request.getDate());
            int evaluateYes = 0;
            int evaluateNo = 0;
            boolean status = false;
            for (EvaluateDate y : evaluateDateList) {
                if (y.getIdCreated() != null && y.getIdCreated() != 0) {
                    evaluateYes++;
                    status = true;
                } else {
                    evaluateNo++;
                }
            }
            response.setEvaluate(status);
            response.setId(x.getId());
            response.setNameClass(x.getClassName());
            response.setTotalKid(evaluateDateList.size());
            response.setEvaluateYes(evaluateYes);
            response.setEvaluateNo(evaluateNo);
            response.setGoSchool(evaluateDateList.size() - absent);
            response.setAbsent(absent);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<EvaluateStatusKidMonthPlusResponse> findEvaluateStatusKidMonth(UserPrincipal principal, EvaluatePlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<EvaluateStatusKidMonthPlusResponse> responseList = new ArrayList<>();
        StartEndDateObject startEndDate = ConvertData.getStartEndDateOfMonth(request.getDate());

        List<MaClass> maClassList = maClassRepository.findClassInGradeWithDate(principal.getIdSchoolLogin(), request.getId(), startEndDate.getEndDate());
        int numberMonth = startEndDate.getEndDate().minusDays(1).getDayOfMonth();
        int year = startEndDate.getStartDate().getYear();
        int month = startEndDate.getStartDate().getMonthValue();

        LocalDate dateNow = LocalDate.now();
        int monthNow = dateNow.getMonthValue();
        int yearNow = dateNow.getYear();

        if ((month > monthNow && year == yearNow) || (year > yearNow)) {
            return responseList;
        } else if (month == monthNow && year == yearNow) {
            numberMonth = dateNow.getDayOfMonth();
        }

        int finalNumberMonth = numberMonth;
        maClassList.forEach(x -> {
            EvaluateStatusKidMonthPlusResponse response = new EvaluateStatusKidMonthPlusResponse();
            int checkMonth = 0;
            for (int i = 0; i < finalNumberMonth; i++) {
                List<EvaluateDate> evaluateDateList = evaluateDateRepository.findByIdClassAndDate(x.getId(), startEndDate.getStartDate().plusDays(i));
                if (!CollectionUtils.isEmpty(evaluateDateList)) {
                    long checkDay = evaluateDateList.stream().filter(y -> EvaluateUtil.checkHasEvaluateDate(y) && y.isApproved()).count();
                    if (checkDay > 0) {
                        checkMonth++;
                    }
                }
            }
            List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findEvaluateWeekOfMonth(x.getId(), startEndDate.getStartDate(), startEndDate.getEndDate());
            List<Long> checkWeek = evaluateWeekList.stream().filter(s -> EvaluateUtil.checkHasEvaluateWeek(s) && s.isApproved()).map(v -> v.getKids().getId()).collect(Collectors.toList());
            checkWeek = List.copyOf(Set.copyOf(checkWeek));

            List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findByEvaluateMonthWithClass(x.getId(), startEndDate.getStartDate().getMonthValue(), startEndDate.getEndDate().getYear());
            List<Long> checkMonthKid = evaluateMonthList.stream().filter(s -> EvaluateUtil.checkHasEvaluateMonth(s) && s.isApproved()).map(v -> v.getKids().getId()).collect(Collectors.toList());
            checkMonthKid = List.copyOf(Set.copyOf(checkMonthKid));


            List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findEvaluatePeriodicOfMonth(startEndDate.getStartDate(), startEndDate.getEndDate(), x.getId());
            List<Long> checkPeriodic = evaluatePeriodicList.stream().filter(s -> s.getIdCreated() != null && s.getIdCreated() != 0 && s.isApproved()).map(v -> v.getKids().getId()).collect(Collectors.toList());
            checkPeriodic = List.copyOf(Set.copyOf(checkPeriodic));

            response.setId(x.getId());
            response.setKidEvaluateWeekYes(checkWeek.size());
            response.setKidEvaluatePeriodicYes(checkPeriodic.size());
            response.setKidEvaluateMonthYes(checkMonthKid.size());
            response.setEvaluateDayYes(checkMonth);
            response.setEvaluateDayNo(finalNumberMonth - checkMonth);
            response.setKidEvaluateNo(checkStatusKid(startEndDate, x.getId(), finalNumberMonth));
            response.setNameClass(x.getClassName());
            responseList.add(response);
        });
        return responseList;
    }

    private int checkStatusKid(StartEndDateObject startEndDateObject, Long idClass, int numberMonth) {
        List<Long> listDate = new ArrayList<>();
        List<Long> listDateAll = new ArrayList<>();
        LocalDate startDate = startEndDateObject.getStartDate();
        LocalDate endDate = startEndDateObject.getEndDate();

        List<EvaluateDate> evaluateDate = evaluateDateRepository.findByIdClassAndDate(idClass, startDate);
        if (!CollectionUtils.isEmpty(evaluateDate)) {
            listDate.addAll(evaluateDate.stream().filter(y -> !EvaluateUtil.checkHasEvaluateDate(y)).map(x -> x.getKids().getId()).collect(Collectors.toList()));
        }
        List<Long> idList1 = evaluateDate.stream().map(e -> e.getKids().getId()).collect(Collectors.toList());
        for (int i = 0; i < numberMonth; i++) {
            List<EvaluateDate> evaluateDateList = evaluateDateRepository.findByIdClassAndDate(idClass, startDate.plusDays(i));
            listDateAll.addAll(this.checkEvaluateDate(listDate, idList1, evaluateDateList));
            listDate = listDateAll;
            idList1 = evaluateDateList.stream().map(e -> e.getKids().getId()).collect(Collectors.toList());
        }
        listDateAll = List.copyOf(Set.copyOf(listDateAll));

        List<Long> kidList = new ArrayList<>();
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findByIdClassAndMonthAndYear(idClass, startDate.getMonthValue(), startDate.getYear());
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findEvaluateWeekOfMonth(idClass, startDate, startDate.plusMonths(1));
        List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findEvaluatePeriodicOfMonth(startDate, startDate.plusMonths(1), idClass);
        for (Long x : listDateAll) {
            boolean checkM = false;
            if (CollectionUtils.isNotEmpty(evaluateMonthList)) {
                if (evaluateMonthList.stream().anyMatch(m -> !m.getKids().getId().equals(x) && !EvaluateUtil.checkHasEvaluateMonth(m))) {
                    checkM = true;
                }
            } else checkM = true;
            boolean checkW = false;
            if (CollectionUtils.isNotEmpty(evaluateWeekList)) {
                if (evaluateWeekList.stream().anyMatch(m -> !m.getKids().getId().equals(x) && !EvaluateUtil.checkHasEvaluateWeek(m))) {
                    checkW = true;
                }
            } else checkW = true;
            boolean checkP = false;
            if (CollectionUtils.isNotEmpty(evaluatePeriodicList)) {
                if (evaluatePeriodicList.stream().anyMatch(m -> !m.getKids().getId().equals(x))) {
                    checkP = true;
                }
            } else checkP = true;

            if (checkM && checkW && checkP) {
                kidList.add(x);
            }
//            if (evaluateMonthList.stream().anyMatch(m -> !m.getKids().getId().equals(x) && !EvaluateUtil.checkHasEvaluateMonth(m)) && evaluateWeekList.stream().anyMatch(w -> !w.getKids().getId().equals(x) && !EvaluateUtil.checkHasEvaluateWeek(w)) && evaluatePeriodicList.stream().anyMatch(p -> !p.getKids().getId().equals(x))) {
//                kidList.add(x);
//            }
        }
        return kidList.size();
    }

    private List<Long> checkEvaluateDate(List<Long> idListNo, List<Long> idListFull, List<EvaluateDate> evaluateDateList) {
        List<Long> idList = new ArrayList<>();
        for (Long x : idListNo) {
            idList = evaluateDateList.stream().filter(y -> y.getKids().getId().equals(x) && !EvaluateUtil.checkHasEvaluateDate(y)).map(a -> a.getKids().getId()).collect(Collectors.toList());
        }
        List<Long> finalIdList = idList;
        evaluateDateList.forEach(y -> {
            if (idListFull.stream().noneMatch(k -> k.equals(y.getKids().getId())) && !EvaluateUtil.checkHasEvaluateDate(y)) {
                finalIdList.add(y.getKids().getId());
            }
        });
        return finalIdList;
    }


    @Override
    public EvaluateStatusResponse getEvaluateStatus(UserPrincipal principal, Long idClass) {
        CommonValidate.checkDataPlus(principal);
        EvaluateStatusResponse model = new EvaluateStatusResponse();
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

    @Transactional
    @Override
    public boolean approvedPeriodic(UserPrincipal principal, IdEvaluateListRequest request) throws FirebaseMessagingException {
        for (Long x : request.getIdList()) {
            EvaluatePeriodic evaluatePeriodic = evaluatePeriodicRepository.findByIdAndIdSchoolAndDelActiveTrue(x, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found evaluateperiodic by id"));
            evaluatePeriodic.setApproved(evaluatePeriodic.isApproved() ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            evaluatePeriodicRepository.save(evaluatePeriodic);
        }

        return true;
    }

    @Transactional
    @Override
    public boolean approvedMonth(UserPrincipal principal, IdEvaluateListRequest request) throws FirebaseMessagingException {
        for (Long x : request.getIdList()) {
            EvaluateMonth evaluateMonth = evaluateMonthRepository.findEvaluateMonthByIdAndIdSchoolAndDelActive(x, principal.getIdSchoolLogin(), AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found evaluatepmonth by id"));
            evaluateMonth.setApproved(evaluateMonth.isApproved() ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            evaluateMonthRepository.save(evaluateMonth);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean approvedWeek(UserPrincipal principal, IdEvaluateListRequest request) throws FirebaseMessagingException {
        for (Long x : request.getIdList()) {
            EvaluateWeek evaluateWeek = evaluateWeekRepository.findEvaluateWeekByIdAndIdSchoolAndDelActive(x, principal.getIdSchoolLogin(), AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found evaluatepweek by id"));
            evaluateWeek.setApproved(evaluateWeek.isApproved() ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            evaluateWeekRepository.save(evaluateWeek);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean approvedDate(UserPrincipal principal, IdEvaluateListRequest request) throws FirebaseMessagingException {
        for (Long x : request.getIdList()) {
            EvaluateDate evaluateDate = evaluateDateRepository.findByIdAndIdSchoolAndDelActive(x, principal.getIdSchoolLogin(), AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found evaluatepdate by id"));
            evaluateDate.setApproved(evaluateDate.isApproved() ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            evaluateDateRepository.save(evaluateDate);
        }
        return true;
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


    private ReplyTypeEditObject setPeriodicReplyResponse(Long idSchool, EvaluatePeriodic evaluate, Long idUser) {
        ReplyTypeEditObject reply = new ReplyTypeEditObject();
        MaUser maUser = maUserRepository.findById(evaluate.getSchoolReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
        reply.setType(AppTypeConstant.TEACHER);
        reply.setEditStatus(evaluate.getSchoolReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        reply.setFullName(evaluate.getSchoolReplyCreatedBy());
        reply.setContent(evaluate.isSchoolReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluate.getSchoolReplyContent());
        reply.setCreatedDate(evaluate.getSchoolReplyDatetime());
        reply.setModifyStatus(evaluate.isSchoolReplyModified());
        reply.setRevoke(evaluate.isSchoolReplyDel());
        return reply;
    }

    private void setPeriodicReplyData(UserPrincipal principal, EvaluatePeriodic evaluate, ContentAndIdMobileRequest
            request) {
        evaluate.setSchoolReplyModified(evaluate.getSchoolReplyIdCreated() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        evaluate.setSchoolReplyContent(request.getContent());
        evaluate.setSchoolReplyIdCreated(principal.getId());
        evaluate.setSchoolReplyCreatedBy(principal.getFullName());
        evaluate.setSchoolReplyDatetime(LocalDateTime.now());
        evaluate.setSchoolReplyDel(AppConstant.APP_FALSE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
    }

    private void setPeriodicReply(Long idSchool, EvaluatePeriodic x, EvaluateKidPlusResponse model, Long idUser) {
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

    private ReplyTypeEditObject setMonthReplyResponse(Long idSchool, EvaluateMonth evaluate, Long idUser) {
        ReplyTypeEditObject reply = new ReplyTypeEditObject();
        MaUser maUser = maUserRepository.findById(evaluate.getSchoolReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
        reply.setType(AppTypeConstant.TEACHER);
        reply.setEditStatus(evaluate.getSchoolReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        reply.setFullName(evaluate.getSchoolReplyCreatedBy());
        reply.setContent(evaluate.isSchoolReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluate.getSchoolReplyContent());
        reply.setCreatedDate(evaluate.getSchoolReplyDatetime());
        reply.setModifyStatus(evaluate.isSchoolReplyModified());
        reply.setRevoke(evaluate.isSchoolReplyDel());
        return reply;
    }

    private void setMonthReplyData(UserPrincipal principal, EvaluateMonth evaluate, ContentAndIdMobileRequest
            request) {
        evaluate.setSchoolReplyModified(evaluate.getSchoolReplyIdCreated() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        evaluate.setSchoolReplyContent(request.getContent());
        evaluate.setSchoolReplyIdCreated(principal.getId());
        evaluate.setSchoolReplyCreatedBy(principal.getFullName());
        evaluate.setSchoolReplyDatetime(LocalDateTime.now());
        evaluate.setSchoolReplyDel(AppConstant.APP_FALSE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
    }

    private void setMonthReply(Long idSchool, EvaluateMonth x, EvaluateKidPlusResponse model, Long idUser) {
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

    private ReplyTypeEditObject setWeekReplyResponse(Long idSchool, EvaluateWeek evaluate, Long idUser) {
        ReplyTypeEditObject reply = new ReplyTypeEditObject();
        MaUser maUser = maUserRepository.findById(evaluate.getSchoolReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
        reply.setType(AppTypeConstant.SCHOOL);
        reply.setEditStatus(evaluate.getSchoolReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        reply.setFullName(evaluate.getSchoolReplyCreatedBy());
        reply.setContent(evaluate.isSchoolReplyDel() ? EvaluateConstant.REVOKE_SCHOOL : evaluate.getSchoolReplyContent());
        reply.setCreatedDate(evaluate.getSchoolReplyDatetime());
        reply.setModifyStatus(evaluate.isSchoolReplyModified());
        reply.setRevoke(evaluate.isSchoolReplyDel());
        return reply;
    }

    private void setWeekReplyData(UserPrincipal principal, EvaluateWeek evaluate, ContentAndIdMobileRequest request) {
        evaluate.setSchoolReplyModified(evaluate.getSchoolReplyIdCreated() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        evaluate.setSchoolReplyContent(request.getContent());
        evaluate.setSchoolReplyIdCreated(principal.getId());
        evaluate.setSchoolReplyCreatedBy(principal.getFullName());
        evaluate.setSchoolReplyDatetime(LocalDateTime.now());
        evaluate.setSchoolReplyDel(AppConstant.APP_FALSE);
        evaluate.setParentRead(AppConstant.APP_FALSE);
    }

    private void setWeekReply(Long idSchool, EvaluateWeek x, EvaluateKidPlusResponse model, Long idUser) {
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

    private ReplyTypeEditObject setDateReplyResponse(Long idSchool, EvaluateDate evaluate, Long idUser) {
        ReplyTypeEditObject reply = new ReplyTypeEditObject();
        MaUser maUser = maUserRepository.findById(evaluate.getSchoolReplyIdCreated()).orElseThrow(() -> new NoSuchElementException("not found MaUser by id of teacher"));
        reply.setType(AppTypeConstant.TEACHER);
        reply.setEditStatus(evaluate.getSchoolReplyIdCreated().equals(idUser) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        reply.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
        reply.setFullName(evaluate.getSchoolReplyCreatedBy());
        reply.setContent(evaluate.isSchoolReplyDel() ? EvaluateConstant.REVOKE_TEACHER : evaluate.getSchoolReplyContent());
        reply.setCreatedDate(evaluate.getSchoolReplyDatetime());
        reply.setModifyStatus(evaluate.isSchoolReplyModified());
        reply.setRevoke(evaluate.isSchoolReplyDel());
        return reply;
    }

    private void setDateReplyData(UserPrincipal principal, EvaluateDate evaluateDate, ContentAndIdMobileRequest
            request) {
        evaluateDate.setSchoolReplyModified(evaluateDate.getSchoolReplyIdCreated() != null ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        evaluateDate.setSchoolReplyContent(request.getContent());
        evaluateDate.setSchoolReplyIdCreated(principal.getId());
        evaluateDate.setSchoolReplyCreatedBy(principal.getFullName());
        evaluateDate.setSchoolReplyDatetime(LocalDateTime.now());
        evaluateDate.setSchoolReplyDel(AppConstant.APP_FALSE);
        evaluateDate.setParentRead(AppConstant.APP_FALSE);
    }

    private void updateParentRead(EvaluateDate evaluateDate) {
        evaluateDate.setSchoolReadReply(AppConstant.APP_TRUE);
        evaluateDateRepository.save(evaluateDate);
    }

    private void setEvaluateDateReply(Long idSchool, EvaluateDate evaluateDate, EvaluateDateKidPlusResponse model, Long idUser) {
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
            reply.setAvatar(AvatarUtils.getAvatarParent(maUser.getId()));
            reply.setFullName(evaluateDate.getParentReplyCreatedBy());
            reply.setContent(evaluateDate.isParentReplyDel() ? EvaluateConstant.REVOKE_PARENT : evaluateDate.getParentReplyContent());
            reply.setCreatedDate(evaluateDate.getParentReplyDatetime());
            reply.setModifyStatus(evaluateDate.isParentReplyModified());
            reply.setRevoke(evaluateDate.isParentReplyDel());
            replyDataList.add(0, reply);
        }
        model.setReplyList(replyDataList);
    }

    private void setProperties(StatisticalOfMonthPlusResponse model, Long idClass, Long idKid, LocalDate
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
     * check sửa về rỗng và cho phép chỉnh sửa nhận xét theo config
     *
     * @param evaluateDB
     * @param request
     */
    private void checkBeforeEditPeriodic(EvaluatePeriodic evaluateDB, EvaluatePeriodicCreatePlusRequest request) {
        if (StringUtils.isBlank(request.getContent())) {
            if (!CollectionUtils.isEmpty(evaluateDB.getEvaluatePeriodicFileList()) && !CollectionUtils.isEmpty(request.getIdFileDeleteList()) && evaluateDB.getEvaluatePeriodicFileList().size() == request.getIdFileDeleteList().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_DELETE);
            }

        }
    }

    private boolean checkChangePeriodic(EvaluatePeriodic evaluate, EvaluatePeriodicCreatePlusRequest dateRequest) {
        if (!evaluate.getContent().equals(dateRequest.getContent()) ||
                !CollectionUtils.isEmpty(dateRequest.getMultipartFileList()) || !CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList())) {
            return true;
        }
        return false;
    }

    /**
     * check trước khi tạo nhận xét định kỳ
     *
     * @param
     * @return
     */
    private ListEvaluateCreatePlusResponse checkBeforeCreatePeriodic(ListEvaluatePeriodicCreatePlusRequest request) {
        ListEvaluateCreatePlusResponse response = new ListEvaluateCreatePlusResponse();
        List<EvaluateCreatePlusResponse> responseList = new ArrayList<>();
        AtomicBoolean checkContent = new AtomicBoolean(false);
        AtomicBoolean checkFile = new AtomicBoolean(false);
        request.getDataList().forEach(x -> {
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
                EvaluateCreatePlusResponse model = new EvaluateCreatePlusResponse();
                if (request.getStatus()) {
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
                    model.setFileList(listMapper.mapList(evaluatePeriodicDB.getEvaluatePeriodicFileList(), MobileFilePlus.class));
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


    /**
     * set model evaluateperiodic
     *
     * @param evaluatePeriodic
     * @return
     */
    private EvaluatePeriodicPlusResponse setModelEvalutePeridoic(EvaluatePeriodic evaluatePeriodic) {
        EvaluatePeriodicPlusResponse model = new EvaluatePeriodicPlusResponse();
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
     * check sửa về rỗng và cho phép chỉnh sửa nhận xét theo config
     *
     * @param evaluateDB
     * @param request
     */
    private void checkBeforeEditMonth(EvaluateMonth evaluateDB, EvaluateCreatePlusRequest request) {
        if (StringUtils.isBlank(request.getContent())
                && CollectionUtils.isEmpty(request.getMultipartFileList())) {
            if (CollectionUtils.isEmpty(evaluateDB.getEvaluateMonthFileList()) || (!CollectionUtils.isEmpty(request.getIdFileDeleteList()) && request.getIdFileDeleteList().size() == evaluateDB.getEvaluateMonthFileList().size())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_DELETE);
            }

        }
    }

    private boolean checkChangeMonth(EvaluateMonth evaluate, EvaluateCreatePlusRequest dateRequest) {
        if (!evaluate.getContent().equals(dateRequest.getContent()) ||
                !CollectionUtils.isEmpty(dateRequest.getMultipartFileList()) || !CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList())) {
            return true;
        }
        return false;
    }

    /**
     * check trước khi tạo dữ liệu tuần
     *
     * @param
     * @return
     */
    private ListEvaluateCreatePlusResponse checkBeforeCreateMonth(ListEvaluateCreatePlusRequest request) {
        ListEvaluateCreatePlusResponse response = new ListEvaluateCreatePlusResponse();
        List<EvaluateCreatePlusResponse> responseList = new ArrayList<>();
        AtomicBoolean checkContent = new AtomicBoolean(false);
        AtomicBoolean checkFile = new AtomicBoolean(false);
        request.getDataList().forEach(x -> {
            EvaluateMonth evaluateMonthDB = evaluateMonthRepository.findById(x.getId()).orElseThrow();
            EvaluateCreatePlusResponse model = new EvaluateCreatePlusResponse();
            //check nội dung khi yêu cầu check là true
            if (request.getStatus()) {
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
                model.setFileList(listMapper.mapList(evaluateMonthDB.getEvaluateMonthFileList(), MobileFilePlus.class));
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

    /**
     * check sửa về rỗng và cho phép chỉnh sửa nhận xét theo config
     *
     * @param evaluateDB
     * @param request
     */
    private void checkBeforeEditWeek(EvaluateWeek evaluateDB, EvaluateCreatePlusRequest request) {
        if (StringUtils.isBlank(request.getContent())
                && CollectionUtils.isEmpty(request.getMultipartFileList())) {
            if (CollectionUtils.isEmpty(evaluateDB.getEvaluateWeekFileList()) || (!CollectionUtils.isEmpty(request.getIdFileDeleteList()) && request.getIdFileDeleteList().size() == evaluateDB.getEvaluateWeekFileList().size())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_DELETE);
            }
        }
    }


    private boolean checkChangeWeek(EvaluateWeek evaluate, EvaluateCreatePlusRequest dateRequest) {
        if (!evaluate.getContent().equals(dateRequest.getContent()) ||
                !CollectionUtils.isEmpty(dateRequest.getMultipartFileList()) || !CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList())) {
            return true;
        }
        return false;
    }

    /**
     * check trước khi tạo dữ liệu tuần
     *
     * @param
     * @return
     */
    private ListEvaluateCreatePlusResponse checkBeforeCreateWeek(ListEvaluateCreatePlusRequest request) {
        ListEvaluateCreatePlusResponse response = new ListEvaluateCreatePlusResponse();
        List<EvaluateCreatePlusResponse> responseList = new ArrayList<>();
        AtomicBoolean checkContent = new AtomicBoolean(false);
        AtomicBoolean checkFile = new AtomicBoolean(false);
        request.getDataList().forEach(x -> {
            EvaluateWeek evaluateWeekDB = evaluateWeekRepository.findById(x.getId()).orElseThrow();
            EvaluateCreatePlusResponse model = new EvaluateCreatePlusResponse();
            if (request.getStatus()) {
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
                model.setFileList(listMapper.mapList(evaluateWeekDB.getEvaluateWeekFileList(), MobileFilePlus.class));
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
     * check sửa về rỗng và cho phép chỉnh sửa nhận xét theo config
     *
     * @param evaluateDateDB
     * @param dateRequest
     */
    private void checkBeforeEditDate(EvaluateDate evaluateDateDB, EvaluateDateCreatePlusRequest dateRequest) {
        if (StringUtils.isBlank(dateRequest.getLearnContent()) && StringUtils.isBlank(dateRequest.getEatContent()) && StringUtils.isBlank(dateRequest.getSleepContent()) && StringUtils.isBlank(dateRequest.getSanitaryContent()) && StringUtils.isBlank(dateRequest.getHealtContent()) && StringUtils.isBlank(dateRequest.getCommonContent())
                && CollectionUtils.isEmpty(dateRequest.getMultipartFileList())) {
            if (CollectionUtils.isEmpty(evaluateDateDB.getEvaluateAttachFileList()) || (!CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList()) && dateRequest.getIdFileDeleteList().size() == evaluateDateDB.getEvaluateAttachFileList().size())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageConstant.EVALUATE_DELETE);
            }
        }
    }


    /**
     * check có chỉnh sửa so với DB hay không
     *
     * @param evaluateDate
     * @param dateRequest
     * @return
     */
    private boolean checkChangeDate(EvaluateDate evaluateDate, EvaluateDateCreatePlusRequest dateRequest) {
        if (!evaluateDate.getLearnContent().equals(dateRequest.getLearnContent()) || !evaluateDate.getEatContent().equals(dateRequest.getEatContent()) ||
                !evaluateDate.getSleepContent().equals(dateRequest.getSleepContent()) || !evaluateDate.getSanitaryContent().equals(dateRequest.getSanitaryContent()) ||
                !evaluateDate.getHealtContent().equals(dateRequest.getHealtContent()) || !evaluateDate.getCommonContent().equals(dateRequest.getCommonContent()) ||
                !CollectionUtils.isEmpty(dateRequest.getMultipartFileList()) || !CollectionUtils.isEmpty(dateRequest.getIdFileDeleteList())) {
            return true;
        }
        return false;
    }

    /**
     * check data trước khi tạo nhận xét
     *
     * @param request
     * @return
     */
    private ListEvaluateDateCreatePlusResponse checkBeforeCreateEvaluateDate
    (ListEvaluateDateCreatePlusRequest request) {
        ListEvaluateDateCreatePlusResponse response = new ListEvaluateDateCreatePlusResponse();
        List<EvaluateDateCreatePlusResponse> responseList = new ArrayList<>();
        AtomicBoolean checkContent = new AtomicBoolean(false);
        AtomicBoolean checkFile = new AtomicBoolean(false);
        request.getDataList().forEach(x -> {
            LocalDateTime localDateTime = x.getDateTime();
            EvaluateDateCreatePlusResponse model = new EvaluateDateCreatePlusResponse();
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

    private String getKidsStatus(Kids kids, Long idClassLogin) {
        return idClassLogin.equals(kids.getMaClass().getId()) ? kids.getKidStatus() : AppConstant.OTHER_CLASS;
    }
}