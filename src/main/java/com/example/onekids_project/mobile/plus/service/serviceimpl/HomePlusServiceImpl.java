package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.school.AppIconPlus;
import com.example.onekids_project.entity.school.AppIconPlusAdd;
import com.example.onekids_project.entity.school.BirthdaySample;
import com.example.onekids_project.entity.school.SchoolInfo;
import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.entity.system.NewsExtra;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EatConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EvaluateConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.LearnConfig;
import com.example.onekids_project.mobile.plus.response.cashinternal.NumberCashInternalResponse;
import com.example.onekids_project.mobile.plus.response.home.*;
import com.example.onekids_project.mobile.plus.response.salary.NumberSalaryPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.CashInternalPlusService;
import com.example.onekids_project.mobile.plus.service.servicecustom.HomePlusService;
import com.example.onekids_project.mobile.plus.service.servicecustom.WalletPlusService;
import com.example.onekids_project.mobile.response.HomeIconResponse;
import com.example.onekids_project.mobile.response.NewsMobileResponse;
import com.example.onekids_project.mobile.service.servicecustom.ChangeInforService;
import com.example.onekids_project.mobile.teacher.response.home.AttendanceKidsConfig;
import com.example.onekids_project.mobile.teacher.response.home.HomeBirthdayResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.SalaryTeacherService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.GradeService;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.util.CommonUtil;
import com.example.onekids_project.util.ConvertConfigUtil;
import com.example.onekids_project.util.DataUtils;
import com.example.onekids_project.util.EmployeeUtil;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomePlusServiceImpl implements HomePlusService {
    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private MaClassService maClassService;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private BirthdaySampleRepository birthdaySampleRepository;

    @Autowired
    private AppIconPlusRepository appIconPlusRepository;

    @Autowired
    private AppIconPlusAddRepository appIconPlusAddRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private MessageParentRepository messageParentRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private AbsentLetterRepository absentLetterRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private EvaluateDateRepository evaluateDateRepository;

    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;

    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;

    @Autowired
    private SmsSendCustomRepository smsSendCustomRepository;

    @Autowired
    private SmsSendRepository smsSendRepository;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsExtraRepository newsExtraRepository;

    @Autowired
    private ChangeInforService changeInforService;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SalaryTeacherService salaryTeacherService;
    @Autowired
    private CashInternalPlusService cashInternalPlusService;
    @Autowired
    private WalletPlusService walletPlusService;
    @Autowired
    private WalletParentHistoryRepository walletParentHistoryRepository;
    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @Override
    public HomeFirstPlusResponse getHomeFirstPlus(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found schoolInfo by id"));
        HomeFirstPlusResponse model = new HomeFirstPlusResponse();
        SchoolConfigPlus schoolConfigPlus = new SchoolConfigPlus();
        SchoolConfigResponse schoolConfigResponse = principal.getSchoolConfig();
        model.setWeekList(DataUtils.getWeekList());
        model.setMonthList(DataUtils.getMonthList());
        model.setQuality(principal.getSysConfig().getQualityPicture());
        model.setWidth(principal.getSysConfig().getWidthPicture());
        model.setGradeList(gradeService.findGradeInSchool(principal));
        model.setClassList(maClassService.findClassInGrade(principal, null));
        EatConfig eatConfig = modelMapper.map(schoolConfigResponse, EatConfig.class);
        LearnConfig learnConfig = ConvertConfigUtil.setLearnConfig(schoolConfigResponse);
        EvaluateConfig evaluateConfig = ConvertConfigUtil.setEvaluateConfig(schoolConfigResponse);
        AttendanceKidsConfig attendanceKidsConfig = ConvertConfigUtil.setAttendanceConfig(schoolConfigResponse);
        schoolConfigPlus.setLearnConfig(learnConfig);
        schoolConfigPlus.setEatConfig(eatConfig);
        schoolConfigPlus.setEvaluateConfig(evaluateConfig);
        schoolConfigPlus.setAttendanceKidsConfig(attendanceKidsConfig);
        schoolConfigPlus.setShowConfigCommonPlus(modelMapper.map(schoolInfo, ShowConfigCommonPlus.class));
        model.setSchoolConfig(schoolConfigPlus);
        return model;
    }

    @Override
    public HomePlusResponse getHomePlus(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        HomePlusResponse response = new HomePlusResponse();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        Employee employee = maUser.getEmployee();
        Long idSchoolLogin = principal.getIdSchoolLogin();
        InfoEmployeeSchool infoEmployeeSchool = EmployeeUtil.convertEmployeeToInfoEmployeeSchool(idSchoolLogin, employee);
        response.setIdSchoolLogin(idSchoolLogin);
        response.setDataLink(CommonUtil.getLink(idSchoolLogin));
        response.setSchoolList(EmployeeUtil.getSchoolList(employee));
        response.setKidsStatistical(this.getKidsStatistical(idSchoolLogin));
        response.setBirthdayList(this.getBirthdayList(principal, employee));
        response.setIconList(this.getIconList(principal, infoEmployeeSchool.getId()));
        //set thông tin bổ sung
        HomePlusExtraResponse extra = new HomePlusExtraResponse();
        SchoolConfigResponse schoolConfig = principal.getSchoolConfig();
        extra.setAlbumMaxNumber(schoolConfig.getAlbumMaxNumber());
        response.setHomePlusExtra(extra);
        return response;
    }

    @Override
    public String changeSchool(UserPrincipal principal, Long idSchool) {
        if (idSchool == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ERROR_INPUT);
        }
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = maUser.getEmployee().getInfoEmployeeSchoolList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        List<InfoEmployeeSchool> inforList = infoEmployeeSchoolList.stream().filter(x -> x.getSchool().getId().equals(idSchool)).collect(Collectors.toList());
        if (inforList.size() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trường đổi không phù hợp");
        }
        Employee employee = maUser.getEmployee();
        employee.setIdSchoolLogin(inforList.get(0).getSchool().getId());
        employeeRepository.save(employee);
        String token = changeInforService.findNewToken(principal);
        return token;
    }

    @Override
    public List<NewsMobileResponse> findNews(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        List<News> newsList = newsRepository.findByAppPlusTrueAndDelActiveTrueOrderByCreatedDateDesc();
        List<NewsMobileResponse> dataList = new ArrayList<>();
        newsList.forEach(x -> {
            NewsMobileResponse model = new NewsMobileResponse();
            model.setDate(x.getCreatedDate().toLocalDate());
            model.setTitle(x.getTitle());
            model.setLink(x.getLink());
            model.setPicture(x.getUrlAttachPicture());
            dataList.add(model);
        });
        NewsExtra newsExtra = newsExtraRepository.findFirstByAppPlusTrueAndDelActiveTrue().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có link mở rộng"));
        NewsMobileResponse model = new NewsMobileResponse();
        model.setDate(newsExtra.getCreatedDate().toLocalDate());
        model.setTitle(newsExtra.getTitle());
        model.setLink(newsExtra.getLink());
        model.setPicture("");
        dataList.add(model);
        return dataList;
    }

    private List<HomeIconResponse> getIconList(UserPrincipal principal, Long idInfoEmployee) {
        List<HomeIconResponse> iconList = new ArrayList<>();
        Long idSchoolLogin = principal.getIdSchoolLogin();
        AppIconPlus appIconPlus = appIconPlusRepository.findBySchoolId(idSchoolLogin).orElseThrow();
        AppIconPlusAdd appIconPlusAdd = appIconPlusAddRepository.findByInfoEmployeeSchoolId(idInfoEmployee).orElseThrow();
        iconList.add(this.getIcon(principal, AppIconName.EMPLOYEE_KEY, AppIconName.EMPLOYEE, appIconPlus.isEmployee() && appIconPlusAdd.isEmployee(), appIconPlus.isEmployeeShow() && appIconPlusAdd.isEmployeeShow(), appIconPlus.getEmployeeLock()));
        iconList.add(this.getIcon(principal, AppIconName.KIDS_KEY, AppIconName.KIDS, appIconPlus.isKids() && appIconPlusAdd.isKids(), appIconPlus.isKidsShow() && appIconPlusAdd.isKidsShow(), appIconPlus.getKidsLock()));
        iconList.add(this.getIcon(principal, AppIconName.SMS_APP_HISTORY_KEY, AppIconName.SMS_APP_HISTORY_NAME, appIconPlus.isSmsAppHistory() && appIconPlusAdd.isSmsAppHistory(), appIconPlus.isSmsAppHistoryShow() && appIconPlusAdd.isSmsAppHistoryShow(), appIconPlus.getSmsAppHistoryLock()));
        iconList.add(this.getIcon(principal, AppIconName.NOTIFY_KEY, AppIconName.NOTIFY_NAME, appIconPlus.isNotify() && appIconPlusAdd.isNotify(), appIconPlus.isNotifyShow() && appIconPlusAdd.isNotifyShow(), appIconPlus.getNotifyLock()));
        iconList.add(this.getIcon(principal, AppIconName.SUPPORT_KEY, AppIconName.SUPPORT_NAME, appIconPlus.isSupport() && appIconPlusAdd.isSupport(), appIconPlus.isSupportShow() && appIconPlusAdd.isSupportShow(), appIconPlus.getSupportLock()));
        iconList.add(this.getIcon(principal, AppIconName.FEEDBACK_KEY, AppIconName.FEEDBACK, appIconPlus.isFeedback() && appIconPlusAdd.isFeedback(), appIconPlus.isFeedbackShow() && appIconPlusAdd.isFeedbackShow(), appIconPlus.getFeedbackLock()));
        iconList.add(this.getIcon(principal, AppIconName.MESSAGE_KEY, AppIconName.MESSAGE, appIconPlus.isMessage() && appIconPlusAdd.isMessage(), appIconPlus.isMessageShow() && appIconPlusAdd.isMessageShow(), appIconPlus.getMessageLock()));
        iconList.add(this.getIcon(principal, AppIconName.MEDICINE_KEY, AppIconName.MEDICINE, appIconPlus.isMedicine() && appIconPlusAdd.isMedicine(), appIconPlus.isMedicineShow() && appIconPlusAdd.isMedicineShow(), appIconPlus.getMedicineLock()));
        iconList.add(this.getIcon(principal, AppIconName.ABSENT_KEY, AppIconName.ABSENT, appIconPlus.isAbsent() && appIconPlusAdd.isAbsent(), appIconPlus.isAbsentShow() && appIconPlusAdd.isAbsentShow(), appIconPlus.getAbsentLock()));
        iconList.add(this.getIcon(principal, AppIconName.ALBUM_KEY, AppIconName.ALBUM, appIconPlus.isAlbum() && appIconPlusAdd.isAlbum(), appIconPlus.isAlbumShow() && appIconPlusAdd.isAlbumShow(), appIconPlus.getAlbumLock()));
        iconList.add(this.getIcon(principal, AppIconName.EVALUATE_KEY, AppIconName.EVALUATE, appIconPlus.isEvaluate() && appIconPlusAdd.isEvaluate(), appIconPlus.isEvaluateShow() && appIconPlusAdd.isEvaluateShow(), appIconPlus.getEvaluateLock()));
        iconList.add(this.getIcon(principal, AppIconName.BIRTHDAY_KEY, AppIconName.BIRTHDAY, appIconPlus.isBirthday() && appIconPlusAdd.isBirthday(), appIconPlus.isBirthdayShow() && appIconPlusAdd.isBirthdayShow(), appIconPlus.getBirthdayLock()));
        iconList.add(this.getIcon(principal, AppIconName.HEALTH_KEY, AppIconName.HEALTH, appIconPlus.isHealth() && appIconPlusAdd.isHealth(), appIconPlus.isHealthShow() && appIconPlusAdd.isHealthShow(), appIconPlus.getHealthLock()));
        iconList.add(this.getIcon(principal, AppIconName.ATTENDANCE_KEY, AppIconName.ATTENDANCE, appIconPlus.isAttendance() && appIconPlusAdd.isAttendance(), appIconPlus.isAttendanceShow() && appIconPlusAdd.isAttendanceShow(), appIconPlus.getAttendanceLock()));
        iconList.add(this.getIcon(principal, AppIconName.STUDENT_FEES_KEY, AppIconName.STUDENT_FEES, appIconPlus.isStudentFees() && appIconPlusAdd.isStudentFees(), appIconPlus.isStudentFeesShow() && appIconPlusAdd.isStudentFeesShow(), appIconPlus.getStudentFeesLock()));
        iconList.add(this.getIcon(principal, AppIconName.VIDEO_KEY, AppIconName.VIDEO, appIconPlus.isVideo() && appIconPlusAdd.isVideo(), appIconPlus.isVideoShow() && appIconPlusAdd.isVideoShow(), appIconPlus.getVideoLock()));
        iconList.add(this.getIcon(principal, AppIconName.LEARN_KEY, AppIconName.LEARN, appIconPlus.isLearn() && appIconPlusAdd.isLearn(), appIconPlus.isLearnShow() && appIconPlusAdd.isLearnShow(), appIconPlus.getLearnLock()));
        iconList.add(this.getIcon(principal, AppIconName.MENU_KEY, AppIconName.MENU, appIconPlus.isMenu() && appIconPlusAdd.isMenu(), appIconPlus.isMenuShow() && appIconPlusAdd.isMenuShow(), appIconPlus.getMenuLock()));
        iconList.add(this.getIcon(principal, AppIconName.CAMERA_KEY, AppIconName.CAMERA, appIconPlus.isCamera() && appIconPlusAdd.isCamera(), appIconPlus.isCameraShow() && appIconPlusAdd.isCameraShow(), appIconPlus.getCameraLock()));
        iconList.add(this.getIcon(principal, AppIconName.UTILITY_KEY, AppIconName.UTILITY, appIconPlus.isUtility() && appIconPlusAdd.isUtility(), appIconPlus.isUtilityShow() && appIconPlusAdd.isUtilityShow(), appIconPlus.getUtilityLock()));
        iconList.add(this.getIcon(principal, AppIconName.SALARY_KEY, AppIconName.SALARY, appIconPlus.isSalary() && appIconPlusAdd.isSalary(), appIconPlus.isSalaryShow() && appIconPlusAdd.isSalaryShow(), appIconPlus.getSalaryLock()));
        iconList.add(this.getIcon(principal, AppIconName.FACEBOOK_KEY, AppIconName.FACEBOOK, appIconPlus.isFacebook() && appIconPlusAdd.isFacebook(), appIconPlus.isFacebookShow() && appIconPlusAdd.isFacebookShow(), appIconPlus.getFacebookLock()));
        iconList.add(this.getIcon(principal, AppIconName.CASH_INTERNAL_KEY, AppIconName.CASH_INTERNAL_NAME, appIconPlus.isCashInternal() && appIconPlusAdd.isCashInternal(), appIconPlus.isCashInternalShow() && appIconPlusAdd.isCashInternalShow(), appIconPlus.getCashInternalLock()));
        iconList.add(this.getIcon(principal, AppIconName.WALLET_KEY, AppIconName.WALLET_NAME, appIconPlus.isWallet() && appIconPlusAdd.isWallet(), appIconPlus.isWalletShow() && appIconPlusAdd.isWalletShow(), appIconPlus.getWalletLock()));
        iconList.add(this.getIcon(principal, AppIconName.NOTIFY_SCHOOL_KEY, AppIconName.NOTIFY_SCHOOL_NAME, appIconPlus.isNotifySchool() && appIconPlusAdd.isNotifySchool(), appIconPlus.isNotifySchoolShow() && appIconPlusAdd.isNotifySchoolShow(), appIconPlus.getNotifyLock()));
        iconList.add(this.getIcon(principal, AppIconName.NEWS_KEY, AppIconName.NEWS_NAME, appIconPlus.isNews() && appIconPlusAdd.isNews(), appIconPlus.isNewsShow() && appIconPlusAdd.isNewsShow(), appIconPlus.getNewsLock()));
        return iconList;
    }

    private HomeIconResponse getIcon(UserPrincipal principal, String key, String value, boolean clickStatus, boolean showStatus, String textLock) {
        Long idSchool = principal.getIdSchoolLogin();
        HomeIconResponse model = new HomeIconResponse();
        model.setKey(key);
        model.setValue(value);
        model.setClickStatus(clickStatus);
        model.setShowStatus(showStatus);
        model.setTextLock(textLock);
        if (clickStatus) {
            switch (key) {
                case AppIconName.FEEDBACK_KEY:
                    model.setNewNotifiNumber(this.getNewNumberFeedback(idSchool));
                    break;
                case AppIconName.MESSAGE_KEY:
                    model.setNewNotifiNumber(this.getNewNumberMessageParent(idSchool));
                    break;
                case AppIconName.MEDICINE_KEY:
                    model.setNewNotifiNumber(this.getNewNumberMedicine(idSchool));
                    break;
                case AppIconName.ABSENT_KEY:
                    model.setNewNotifiNumber(this.getNewNumberAbsent(idSchool));
                    break;
                case AppIconName.ALBUM_KEY:
                    model.setNewNotifiNumber(this.getNewNumberAlbum(idSchool));
                    break;
                case AppIconName.EVALUATE_KEY:
                    model.setNewNotifiNumber(this.getNewNumberEvaluate(idSchool));
                    break;
                case AppIconName.BIRTHDAY_KEY:
                    model.setNewNotifiNumber(this.getNewNumberBirthday(idSchool));
                    break;
                case AppIconName.NOTIFY_KEY:
                    model.setNewNotifiNumber(this.getNewNotifyUnread(idSchool, principal.getId()));
                    break;
                case AppIconName.SMS_APP_HISTORY_KEY:
                    model.setNewNotifiNumber(this.getNewSMSAppHistory(idSchool));
                    break;
                case AppIconName.SALARY_KEY:
                    model.setNewNotifiNumber(this.getSalaryNumber(principal));
                    break;
                case AppIconName.CASH_INTERNAL_KEY:
                    model.setNewNotifiNumber(this.getCashInternalNumber(principal));
                    break;
                case AppIconName.WALLET_KEY:
                    model.setNewNotifiNumber(this.getWalletUnConfirmNumber(principal));
                    break;
            }
        }
        return model;
    }

//    private List<SchoolPlusResponse> getSchoolList(Employee employee) {
//        List<SchoolPlusResponse> dataList = new ArrayList<>();
//        List<InfoEmployeeSchool> infoEmployeeSchoolList = employee.getInfoEmployeeSchoolList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
//        infoEmployeeSchoolList.forEach(x -> {
//            SchoolPlusResponse model = new SchoolPlusResponse();
//            model.setId(x.getSchool().getId());
//            model.setPlusName(x.getFullName());
//            model.setSchoolName(x.getSchool().getSchoolName());
//            model.setAvatar(AvatarUtils.getAvatarInfoEmployee(x));
//            dataList.add(model);
//        });
//        return dataList;
//    }

    private KidsStatisticalResponse getKidsStatistical(Long idSchool) {
        LocalDate nowDate = LocalDate.now();
        KidsStatisticalResponse model = new KidsStatisticalResponse();
        List<String> statusList = kidsRepository.countWithStatus(idSchool);
        int kidsTotal = (int) statusList.stream().filter(x -> !x.equals(KidsStatusConstant.LEAVE_SCHOOL)).count();
        int studying = (int) statusList.stream().filter(x -> x.equals(KidsStatusConstant.STUDYING)).count();
        model.setKidTotal(kidsTotal);
        model.setKidStudy(studying);
        //số học sinh được tạo điểm danh cho ngày đó
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByIdSchoolAndAttendanceDate(idSchool, nowDate);
        //số học sinh đã được điểm danh đến
        List<AttendanceKids> attendaceYesList = attendanceKidsList.stream().filter(AttendanceKids::isAttendanceArrive).collect(Collectors.toList());
        model.setKidAttendanceYes(attendaceYesList.size());
        model.setKidAttendanceNo(attendanceKidsList.size() - attendaceYesList.size());
        //số học sinh đã được điểm danh đi học
        List<AttendanceKids> goSchoolList = this.getAttendanceGoSchool(attendaceYesList);
        model.setKidGoSchool(goSchoolList.size());
        model.setKidLeaveSchool((int) goSchoolList.stream().filter(AttendanceKids::isAttendanceLeave).count());
        return model;
    }

    private List<AttendanceKids> getAttendanceGoSchool(List<AttendanceKids> attendanceKidsList) {
        return attendanceKidsList.stream().filter(x -> x.getAttendanceArriveKids().isMorning() || x.getAttendanceArriveKids().isAfternoon() || x.getAttendanceArriveKids().isEvening()).collect(Collectors.toList());
    }

    private List<HomeBirthdayResponse> getBirthdayList(UserPrincipal principal, Employee employee) {
        List<HomeBirthdayResponse> responseList = new ArrayList<>();
        Optional<BirthdaySample> birthdaySampleOptional = birthdaySampleRepository.findByIdSchoolAndBirthdayTypeAndActiveTrue(0L, SampleConstant.PLUS);
        if (birthdaySampleOptional.isPresent()) {
            LocalDate nowDate = LocalDate.now();
            BirthdaySample birthdaySample = birthdaySampleOptional.get();
            List<InfoEmployeeSchool> infoEmployeeSchoolList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.isDelActive() && x.isActivated()).collect(Collectors.toList());
            infoEmployeeSchoolList.forEach(x -> {
                HomeBirthdayResponse model = new HomeBirthdayResponse();
                if (x.getBirthday().getDayOfMonth() == nowDate.getDayOfMonth() && x.getBirthday().getMonthValue() == nowDate.getMonthValue()) {
                    String content = birthdaySample.getContent();
                    content = content.replace(SampleConstant.NAME, principal.getFullName());
                    String picture = StringUtils.isNotBlank(birthdaySample.getUrlPicture()) ? birthdaySample.getUrlPicture() : PictureConstant.BIRTHDAY_SCHOOL;
                    model.setContent(content);
                    model.setPicture(picture);
                    model.setStatus(AppConstant.APP_TRUE);
                    responseList.add(model);
                }
            });
        }
        return responseList;
    }

    private int getNewNumberFeedback(Long idSchool) {
        return feedBackRepository.countByIdSchoolAndSchoolConfirmStatusFalse(idSchool);
    }

    private int getNewNumberMessageParent(Long idSchool) {
        return messageParentRepository.countByIdSchoolAndParentMessageDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(idSchool);
    }

    private int getNewNumberMedicine(Long idSchool) {
        return medicineRepository.countByIdSchoolAndParentMedicineDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(idSchool);
    }

    private int getNewNumberAbsent(Long idSchool) {
        return absentLetterRepository.countByIdSchoolAndParentAbsentDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(idSchool);
    }

    private int getNewNumberAlbum(Long idSchool) {
        int count = 0;
        List<Album> albumList = albumRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        for (Album x : albumList) {
            long countApproved = x.getAlistPictureList().stream().filter(ListPicture::getIsApproved).count();
            if (countApproved == 0) {
                count++;
            }
        }
        return count;
    }

    private int getNewNumberBirthday(Long idSchool) {
        return kidsRepository.countByIdSchoolAndBirthDayAndKidStatusAndDelActiveTrue(idSchool, LocalDate.now(), KidsStatusConstant.STUDYING);
    }

    private int getNewSMSAppHistory(Long idSchool) {
        return appSendRepository.countByIdSchoolAndIsApprovedFalseAndSendTypeAndDelActiveTrueAndSendDelFalse(idSchool, AppConstant.SEND_TYPE_COMMON);
    }

    private int getNewNotifyUnread(Long idSchool, Long idUser) {
        return receiversRepository.countByIdUserReceiverAndIdSchoolAndUserUnreadFalseAndSendDelFalseAndDelActiveTrue(idUser, idSchool);
    }

    private int getSalaryNumber(UserPrincipal principal) {
        NumberSalaryPlusResponse response = salaryTeacherService.showNumberPlus(principal);
        return response.getAbsentNumber();
    }

    private int getCashInternalNumber(UserPrincipal principal) {
        NumberCashInternalResponse response = cashInternalPlusService.getShowNumber(principal);
        return response.getInNumber() + response.getOutNumber();
    }

    private int getWalletUnConfirmNumber(UserPrincipal principal) {
//        List<WalletClassPlusResponse> responseList = walletPlusService.getWalletClass(principal.getIdSchoolLogin());
//                return responseList.stream().mapToInt(x -> x.getParentNoConfirm() + x.getSchoolNoConfirm()).sum();
        return walletParentHistoryRepository.countByWalletParentIdSchoolAndConfirmFalse(principal.getIdSchoolLogin());


    }

    /**
     * lấy số nhận xét của mỗi lớp có ít nhất 1 nhận xét chưa duyệt
     *
     * @param idSchool
     * @return
     */
    private int getNewNumberEvaluate(Long idSchool) {
//        LocalDate nowDate = LocalDate.now();
//        LocalDate monday = ConvertData.getMondayOfWeek(nowDate);
//        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        int count = 0;
//        for (MaClass x : maClassList) {
//            List<EvaluateDate> evaluateDateList = evaluateDateRepository.findClassDateHas(x.getId(), nowDate);
//            List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findEvaluateWeekClassWidthDate(x.getId(), monday);
//            List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findEvaluateMonthClassWidthDate(x.getId(), nowDate);
//            List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findClassDateHas(x.getId(), nowDate);
//            if (evaluateDateList.size() > 0 || evaluateWeekList.size() > 0 || evaluateMonthList.size() > 0 || evaluatePeriodicList.size() > 0) {
//                count++;
//            }
//        }
        return count;
    }

}
