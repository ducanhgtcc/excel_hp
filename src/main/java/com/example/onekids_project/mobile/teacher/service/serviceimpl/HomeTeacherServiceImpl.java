package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.common.PictureConstant;
import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.classes.ListPicture;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Media;
import com.example.onekids_project.entity.school.*;
import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.entity.system.NewsExtra;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mobile.parent.response.schoolconfig.AbsentConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EatConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EvaluateConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.LearnConfig;
import com.example.onekids_project.mobile.response.ChangeTokenResponse;
import com.example.onekids_project.mobile.response.HomeIconResponse;
import com.example.onekids_project.mobile.response.LinkResponse;
import com.example.onekids_project.mobile.response.NewsMobileResponse;
import com.example.onekids_project.mobile.service.servicecustom.ChangeInforService;
import com.example.onekids_project.mobile.teacher.response.absentletter.NumberSalaryTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.home.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.HomeTeacherService;
import com.example.onekids_project.mobile.teacher.service.servicecustom.SalaryTeacherService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SysInforService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeTeacherServiceImpl implements HomeTeacherService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private SysInforRepository sysInforRepository;

    @Autowired
    private BirthdaySampleRepository birthdaySampleRepository;

    @Autowired
    private AppIconTeacherRepository appIconTeacherRepository;

    @Autowired
    private AppIconTeacherAddRepository appIconTeacherAddRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private MessageParentRepository messageParentRepository;

    @Autowired
    private EvaluateDateRepository evaluateDateRepository;

    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;

    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;

    @Autowired
    private EvaluatePeriodicRepository evaluatePeriodicRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private AbsentLetterRepository absentLetterRepository;

    @Autowired
    private NewsExtraRepository newsExtraRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private ExEmployeeClassRepository exEmployeeClassRepository;

    @Autowired
    private SysInforService sysInforService;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ExAlbumKidsRepository exAlbumKidsRepository;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ChangeInforService changeInforService;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SalaryTeacherService salaryTeacherService;

    @Override
    public HomeFirstTeacherResponse findHomeFirstTeacher(UserPrincipal principal) {
        CommonValidate.checkDataNoClassTeacher(principal);
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id"));
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found schoolInfo by id"));
        HomeFirstTeacherResponse model = new HomeFirstTeacherResponse();
        SchoolConfigTeacher schoolConfigTeacher = new SchoolConfigTeacher();
        SchoolConfigResponse schoolConfigResponse = principal.getSchoolConfig();
        Long idSchool = principal.getIdSchoolLogin();
        Employee employee = maUser.getEmployee();
        InfoEmployeeSchool infoEmployeeSchool = EmployeeUtil.convertEmployeeToInfoEmployeeSchool(idSchool, employee);
        LocalDate dateStart = infoEmployeeSchool.getStartDate();
        model.setWeekList(DataUtils.getWeekList());
        model.setMonthList(DataUtils.getMonthList());
        model.setQuality(principal.getSysConfig().getQualityPicture());
        model.setWidth(principal.getSysConfig().getWidthPicture());
        EatConfig eatConfig = modelMapper.map(schoolConfigResponse, EatConfig.class);
        LearnConfig learnConfig = ConvertConfigUtil.setLearnConfig(schoolConfigResponse);
        EvaluateConfig evaluateConfig = ConvertConfigUtil.setEvaluateConfig(schoolConfigResponse);
        AbsentConfig absentConfig = ConvertConfigUtil.setAbsentConfig(schoolConfigResponse);
        AttendanceKidsConfig attendanceKidsConfig = ConvertConfigUtil.setAttendanceConfig(schoolConfigResponse);
        CommonConfigTeacher commonConfigTeacher = ConvertConfigUtil.setCommonConfigTeacher(schoolConfigResponse);
        schoolConfigTeacher.setLearnConfig(learnConfig);
        schoolConfigTeacher.setEatConfig(eatConfig);
        schoolConfigTeacher.setEvaluateConfig(evaluateConfig);
        schoolConfigTeacher.setAbsentConfig(absentConfig);
        schoolConfigTeacher.setAttendanceKidsConfig(attendanceKidsConfig);
        schoolConfigTeacher.setCommonConfig(commonConfigTeacher);
        schoolConfigTeacher.setShowConfigCommonTeacher(modelMapper.map(schoolInfo, ShowConfigCommonTeacher.class));

        model.setSchoolConfig(schoolConfigTeacher);
        return model;
    }

    @Override
    public HomeTeacherResponse findHomeTeacher(UserPrincipal principal) {
        CommonValidate.checkDataNoClassTeacher(principal);
        HomeTeacherResponse model = new HomeTeacherResponse();
        Long idSchool = principal.getIdSchoolLogin();
        Long idClass = principal.getIdClassLogin();
        School school = schoolRepository.findById(idSchool).orElseThrow();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in home"));
        Employee employee = maUser.getEmployee();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = EmployeeUtil.getInfoEmployeeList(employee);
        InfoEmployeeSchool infoEmployeeSchool = EmployeeUtil.convertEmployeeToInfoEmployeeSchool(idSchool, employee);
        model.setTeacherName(principal.getFullName());
        model.setSchoolName(school.getSchoolName());
        model.setIdSchoolLogin(idSchool);
        model.setIdClassLogin(idClass);
        model.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, principal.getId()));
        String linkFacebook = this.getLinkFacebook(principal.getIdClassLogin());
        LinkResponse dataLink = this.getLink();
        List<HomeClassResponse> classList = this.getClass(infoEmployeeSchoolList);
        List<HomeBirthdayResponse> birthdayList = this.getBirthday(infoEmployeeSchool, principal);
        List<HomeIconResponse> iconList = this.getIconList(principal, infoEmployeeSchool);
        ChangeTokenResponse changeTokenResponse = this.getNewToken(principal, infoEmployeeSchool, classList);
        model.setLinkFacebook(linkFacebook);
        model.setDataLink(dataLink);
        model.setClassList(classList);
        model.setBirthdayList(birthdayList);
        model.setIconList(iconList);
        model.setDataToken(changeTokenResponse);

        //set thông tin bổ sung
        HomeTeacherExtraResponse extra = new HomeTeacherExtraResponse();
        SchoolConfigResponse schoolConfig = principal.getSchoolConfig();
        extra.setAlbumMaxNumber(schoolConfig.getAlbumMaxNumber());
        model.setHomeTeacherExtra(extra);
        return model;
    }

    @Override
    public int countNotifyUnread(Long idUser) {
        return receiversRepository.countByIdUserReceiverAndIdSchoolAndIsApprovedTrueAndUserUnreadFalseAndSendDelFalseAndDelActiveTrue(SchoolUtils.getIdSchool(), idUser);
    }

    @Override
    public ChangeTokenResponse changeClass(UserPrincipal principal, Long idClass) {
        CommonValidate.checkDataNoClassTeacher(principal);
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in change kid"));
        Employee employee = maUser.getEmployee();

        ChangeTokenResponse model = new ChangeTokenResponse();
//        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolList.get(0);
//        List<ExEmployeeClass> exEmployeeClassList = infoEmployeeSchool.getExEmployeeClassList();
//        Long oldIdClass = principal.getIdClassLogin();
        Long newIdClass = idClass;
//        MaClass oldClass;
        MaClass newClass = maClassRepository.findByIdAndDelActiveTrue(newIdClass).orElseThrow(() -> new NotFoundException("not found maClass by id"));
        List<InfoEmployeeSchool> infoEmployeeSchoolList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.isDelActive() && x.isActivated() && x.getSchool().getId().equals(principal.getIdSchoolLogin())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tồn tại giáo viên chưa bị xóa và kích hoạt trong trường");
        }
//        if (oldIdClass != 0) {
//            oldClass = maClassRepository.findByIdAndDelActiveTrue(oldIdClass).orElseThrow(() -> new NotFoundException("not found maClass by id"));
        model.setSameSchool(employee.getIdSchoolLogin().equals(newClass.getIdSchool()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
//        }
//        if (CollectionUtils.isEmpty(exEmployeeClassList)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giáo viên không có lớp nào");
//        }
        List<Long> idClassList = EmployeeUtil.getIdClassFromEmployee(employee);
//        List<Long> idClassList = exEmployeeClassList.stream().map(x -> x.getMaClass().getId()).collect(Collectors.toList());
        long count = idClassList.stream().filter(x -> x.equals(newIdClass)).count();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id Lớp không hợp lệ");
        }
        employee.setIdClassLogin(newIdClass);
        employee.setIdSchoolLogin(newClass.getIdSchool());
        employeeRepository.save(employee);
        String token = changeInforService.findNewToken(principal);
        model.setToken(token);
        return model;
    }

    @Override
    public List<NewsMobileResponse> findNews(UserPrincipal principal) {
        CommonValidate.checkDataNoClassTeacher(principal);
        List<News> newsList = newsRepository.findByAppTeacherTrueAndDelActiveTrueOrderByCreatedDateDesc();
        List<NewsMobileResponse> dataList = new ArrayList<>();
        newsList.forEach(x -> {
            NewsMobileResponse model = new NewsMobileResponse();
            model.setDate(x.getCreatedDate().toLocalDate());
            model.setTitle(x.getTitle());
            model.setLink(x.getLink());
            model.setPicture(x.getUrlAttachPicture());
            dataList.add(model);
        });
        NewsExtra newsExtra = newsExtraRepository.findFirstByAppTeacherTrueAndDelActiveTrue().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không có link mở rộng"));
        NewsMobileResponse model = new NewsMobileResponse();
        model.setDate(newsExtra.getCreatedDate().toLocalDate());
        model.setTitle(newsExtra.getTitle());
        model.setLink(newsExtra.getLink());
        model.setPicture("");
        dataList.add(model);
        return dataList;
    }

    private ChangeTokenResponse getNewToken(UserPrincipal principal, InfoEmployeeSchool infoEmployeeSchool, List<HomeClassResponse> homeClassResponseList) {
        ChangeTokenResponse changeTokenResponse = new ChangeTokenResponse();
        String newToken = "";
        boolean sameSchool = AppConstant.APP_TRUE;
        //khi không còn lớp nào mới trả ra token
        Long idClassLogin = principal.getIdClassLogin();
        if (idClassLogin != 0) {
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassLogin).orElseThrow();
            //lớp và trường khác nhau
            long count = homeClassResponseList.stream().filter(x -> x.getIdClass().equals(idClassLogin)).count();
            if (!maClass.getIdSchool().equals(principal.getIdSchoolLogin())) {
                sameSchool = AppConstant.APP_FALSE;
                newToken = changeInforService.findNewToken(principal);
                //Lớp trong token ko giống danh sách lớp hoặc không còn lớp nào ở trường hiện tại
            } else if (count == 0 || CollectionUtils.isEmpty(infoEmployeeSchool.getExEmployeeClassList())) {
                newToken = changeInforService.findNewToken(principal);
            }
        } else {
            //id trong token bằng 0 mà idClassLogin trong DB khác 0
            if (infoEmployeeSchool.getEmployee().getIdClassLogin() != 0) {
                newToken = changeInforService.findNewToken(principal);
            }
        }
        changeTokenResponse.setToken(newToken);
        changeTokenResponse.setSameSchool(sameSchool);
        return changeTokenResponse;
    }


    private List<HomeIconResponse> getIconList(UserPrincipal principal, InfoEmployeeSchool infoEmployeeSchool) {
        List<HomeIconResponse> iconList = new ArrayList<>();
        Long idClass = principal.getIdClassLogin();
        AppIconTeacher appIconTeacher = appIconTeacherRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("not found appIconParent by id"));
        AppIconTeacherAdd appIconTeacherAdd = appIconTeacherAddRepository.findByIdSchoolAndInfoEmployeeSchoolIdAndDelActiveTrue(principal.getIdSchoolLogin(), infoEmployeeSchool.getId()).orElseThrow(() -> new NotFoundException("not found AppIconParentAdd by id"));
        List<ExEmployeeClass> exEmployeeClassList = infoEmployeeSchool.getExEmployeeClassList();
        Long finalIdClass = idClass;
        long count = exEmployeeClassList.stream().filter(x -> x.getMaClass().getId().equals(finalIdClass)).count();
        //khi class trong token bị xóa thì trả lại các thông tin icon như khi không có lớp nào
        idClass = count == 0 ? 0L : idClass;
        iconList.add(this.getIcon(principal, AppIconName.FEEDBACK_KEY, AppIconName.FEEDBACK, appIconTeacher.isFeedback() && appIconTeacherAdd.isFeedback(), appIconTeacher.isFeedbackShow() && appIconTeacherAdd.isFeedbackShow(), appIconTeacher.getFeedbackLock()));
        iconList.add(this.getIcon(principal, AppIconName.MESSAGE_KEY, AppIconName.MESSAGE, appIconTeacher.isMessage() && appIconTeacherAdd.isMessage(), appIconTeacher.isMessageShow() && appIconTeacherAdd.isMessageShow(), appIconTeacher.getMessageLock()));
        iconList.add(this.getIcon(principal, AppIconName.MEDICINE_KEY, AppIconName.MEDICINE, appIconTeacher.isMedicine() && appIconTeacherAdd.isMedicine(), appIconTeacher.isMedicineShow() && appIconTeacherAdd.isMedicineShow(), appIconTeacher.getMedicineLock()));
        iconList.add(this.getIcon(principal, AppIconName.ABSENT_KEY, AppIconName.ABSENT, appIconTeacher.isAbsent() && appIconTeacherAdd.isAbsent(), appIconTeacher.isAbsentShow() && appIconTeacherAdd.isAbsentShow(), appIconTeacher.getAbsentLock()));
        iconList.add(this.getIcon(principal, AppIconName.ALBUM_KEY, AppIconName.ALBUM, appIconTeacher.isAlbum() && appIconTeacherAdd.isAlbum(), appIconTeacher.isAlbumShow() && appIconTeacherAdd.isAlbumShow(), appIconTeacher.getAlbumLock()));
        iconList.add(this.getIcon(principal, AppIconName.EVALUATE_KEY, AppIconName.EVALUATE, appIconTeacher.isEvaluate() && appIconTeacherAdd.isEvaluate(), appIconTeacher.isEvaluateShow() && appIconTeacherAdd.isEvaluateShow(), appIconTeacher.getEvaluateLock()));
        iconList.add(this.getIcon(principal, AppIconName.BIRTHDAY_KEY, AppIconName.BIRTHDAY, appIconTeacher.isBirthday() && appIconTeacherAdd.isBirthday(), appIconTeacher.isBirthdayShow() && appIconTeacherAdd.isBirthdayShow(), appIconTeacher.getBirthdayLock()));
        iconList.add(this.getIcon(principal, AppIconName.HEALTH_KEY, AppIconName.HEALTH, appIconTeacher.isHealth() && appIconTeacherAdd.isHealth(), appIconTeacher.isHealthShow() && appIconTeacherAdd.isHealthShow(), appIconTeacher.getHealthLock()));
        iconList.add(this.getIcon(principal, AppIconName.ATTENDANCE_KEY, AppIconName.ATTENDANCE, appIconTeacher.isAttendance() && appIconTeacherAdd.isAttendance(), appIconTeacher.isAttendanceShow() && appIconTeacherAdd.isAttendanceShow(), appIconTeacher.getAttendanceLock()));
        iconList.add(this.getIcon(principal, AppIconName.STUDENT_FEES_KEY, AppIconName.STUDENT_FEES, appIconTeacher.isStudentFees() && appIconTeacherAdd.isStudentFees(), appIconTeacher.isStudentFeesShow() && appIconTeacherAdd.isStudentFeesShow(), appIconTeacher.getStudentFeesLock()));
        iconList.add(this.getIcon(principal, AppIconName.VIDEO_KEY, AppIconName.VIDEO, appIconTeacher.isVideo() && appIconTeacherAdd.isVideo(), appIconTeacher.isVideoShow() && appIconTeacherAdd.isVideoShow(), appIconTeacher.getVideoLock()));
        iconList.add(this.getIcon(principal, AppIconName.LEARN_KEY, AppIconName.LEARN, appIconTeacher.isLearn() && appIconTeacherAdd.isLearn(), appIconTeacher.isLearnShow() && appIconTeacherAdd.isLearnShow(), appIconTeacher.getLearnLock()));
        iconList.add(this.getIcon(principal, AppIconName.MENU_KEY, AppIconName.MENU, appIconTeacher.isMenu() && appIconTeacherAdd.isMenu(), appIconTeacher.isMenuShow() && appIconTeacherAdd.isMenuShow(), appIconTeacher.getMenuLock()));
        iconList.add(this.getIcon(principal, AppIconName.CAMERA_KEY, AppIconName.CAMERA, appIconTeacher.isCamera() && appIconTeacherAdd.isCamera(), appIconTeacher.isCameraShow() && appIconTeacherAdd.isCameraShow(), appIconTeacher.getCameraLock()));
        iconList.add(this.getIcon(principal, AppIconName.UTILITY_KEY, AppIconName.UTILITY, appIconTeacher.isUtility() && appIconTeacherAdd.isUtility(), appIconTeacher.isUtilityShow() && appIconTeacherAdd.isUtilityShow(), appIconTeacher.getUtilityLock()));
        iconList.add(this.getIcon(principal, AppIconName.SALARY_KEY, AppIconName.SALARY, appIconTeacher.isSalary() && appIconTeacherAdd.isSalary(), appIconTeacher.isSalaryShow() && appIconTeacherAdd.isSalaryShow(), appIconTeacher.getSalaryLock()));
        iconList.add(this.getIcon(principal, AppIconName.FACEBOOK_KEY, AppIconName.FACEBOOK, appIconTeacher.isFacebook() && appIconTeacherAdd.isFeedback(), appIconTeacher.isFacebookShow() && appIconTeacherAdd.isFeedbackShow(), appIconTeacher.getFacebookLock()));
        iconList.add(this.getIcon(principal, AppIconName.NEWS_KEY, AppIconName.NEWS_NAME, appIconTeacher.isNews() && appIconTeacherAdd.isNews(), appIconTeacher.isNewsShow() && appIconTeacherAdd.isNewsShow(), appIconTeacher.getNewsLock()));
        return iconList;
    }

    private HomeIconResponse getIcon(UserPrincipal principal, String key, String value, boolean clickStatus, boolean showStatus, String textLock) {
        Long id = principal.getId();
        Long idClass = principal.getIdClassLogin();
        HomeIconResponse model = new HomeIconResponse();
        model.setKey(key);
        model.setValue(value);
        model.setClickStatus(clickStatus);
        model.setShowStatus(showStatus);
        model.setTextLock(textLock);
        if (!AppIconName.FEEDBACK_KEY.equals(key)) {
            if (idClass == 0) {
                model.setClickStatus(AppConstant.APP_FALSE);
                model.setShowStatus(AppConstant.APP_FALSE);
            }
        }
        if (clickStatus) {
            switch (key) {
                case AppIconName.FEEDBACK_KEY:
                    model.setNewNotifiNumber(this.getNewNumberFeedback(id));
                    break;
                case AppIconName.MESSAGE_KEY:
                    model.setNewNotifiNumber(this.getNewNumberMessageParent(idClass));
                    break;
                case AppIconName.MEDICINE_KEY:
                    model.setNewNotifiNumber(this.getNewNumberMedicine(idClass));
                    break;
                case AppIconName.ABSENT_KEY:
                    model.setNewNotifiNumber(this.getNewNumberAbsent(idClass));
                    break;
                case AppIconName.ALBUM_KEY:
                    model.setNewNotifiNumber(this.getNewNumberAlbum(idClass));
                    break;
                case AppIconName.EVALUATE_KEY:
                    model.setNewNotifiNumber(this.getNewNumberEvaluate(idClass));
                    break;
                case AppIconName.BIRTHDAY_KEY:
                    model.setNewNotifiNumber(this.getNewNumberBirthday(idClass));
                    break;
                case AppIconName.SALARY_KEY:
                    model.setNewNotifiNumber(this.getNewNumberSalary(principal));
                    break;
            }
        }
        return model;
    }

    private int getNewNumberFeedback(Long id) {
        return feedBackRepository.countByIdCreatedAndParentUnreadFalse(id);
    }

    private int getNewNumberMessageParent(Long idClass) {
        return messageParentRepository.countByIdClassAndParentMessageDelFalseAndConfirmStatusFalseAndDelActiveTrueAndDelActiveTrue(idClass);
    }

    private int getNewNumberMedicine(Long idClass) {
        return medicineRepository.countByIdClassAndParentMedicineDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(idClass);
    }

    private int getNewNumberAbsent(Long idClass) {
        return absentLetterRepository.countByIdClassAndParentAbsentDelFalseAndConfirmStatusFalseAndDelActiveTrueAndKidsDelActiveTrue(idClass);
    }

    private int getNewNumberAlbum(Long idClass) {
        int count = 0;
        List<Album> albumList = albumRepository.findByMaClassIdAndDelActiveTrue(idClass);
        for (Album x : albumList) {
            long countApproved = x.getAlistPictureList().stream().filter(ListPicture::getIsApproved).count();
            if (countApproved == 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * lấy số nhận xét có phụ huynh phản hồi mà nhà trường chưa đọc trong 1 tháng
     *
     * @param idClass
     * @return
     */
    private int getNewNumberEvaluate(Long idClass) {
        LocalDate nowDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(nowDate.getYear(), nowDate.getMonthValue(), 1);
        LocalDate endDate = startDate.plusMonths(1);
        List<Long> idKidDateList = evaluateDateRepository.findIdKidOfMonthList(idClass, startDate, endDate);
        List<Long> idKidMonthList = evaluateMonthRepository.findIdKidOfMonthList(idClass, nowDate.getMonthValue(), nowDate.getYear());
        List<Long> idKidList = ConvertData.intersecionList(idKidDateList, idKidMonthList);
        int countUnread = 0;
        for (Long idKid : idKidList) {
            int countDateKid = evaluateDateRepository.countSchoolUnreadOfMonth(idKid, startDate, endDate, idClass);
            int countWeekKid = evaluateWeekRepository.countSchoolUnreadOfMonth(idKid, startDate, endDate, idClass);
            int countMonthKid = evaluateMonthRepository.countSchoolUnreadOfMonth(idKid, startDate, idClass);
            int countPeriodicKid = evaluatePeriodicRepository.countSchoolUnreadOfMonth(idKid, startDate, endDate, idClass);
            if (countDateKid > 0 || countWeekKid > 0 || countMonthKid > 0 || countPeriodicKid > 0) {
                countUnread++;
            }
        }
        return countUnread;
    }

    private int getNewNumberBirthday(Long idClass) {
        return kidsRepository.getKidsBirthdayInClass(idClass);
    }

    private int getNewNumberSalary(UserPrincipal principal) {
        NumberSalaryTeacherResponse response = salaryTeacherService.showNumberSalary(principal);
        return response.getAbsentNumber() + response.getSalaryNumber();
    }


    private List<HomeBirthdayResponse> getBirthday(InfoEmployeeSchool infoEmployeeSchool, UserPrincipal principal) {
        List<HomeBirthdayResponse> birthdayList = new ArrayList<>();
        LocalDate nowDate = LocalDate.now();
        Optional<BirthdaySample> birthdaySampleOptional = birthdaySampleRepository.findByIdSchoolAndBirthdayTypeAndActiveTrue(principal.getIdSchoolLogin(), SampleConstant.TEACHER);
        if (birthdaySampleOptional.isPresent()) {
            BirthdaySample birthdaySample = birthdaySampleOptional.get();
            if (infoEmployeeSchool.getBirthday().getDayOfMonth() == nowDate.getDayOfMonth() && infoEmployeeSchool.getBirthday().getMonthValue() == nowDate.getMonthValue()) {
                HomeBirthdayResponse model = new HomeBirthdayResponse();
                String content = birthdaySample.getContent();
                content = content.replace(SampleConstant.NAME, principal.getFullName());
                String picture = StringUtils.isNotBlank(birthdaySample.getUrlPicture()) ? birthdaySample.getUrlPicture() : PictureConstant.BIRTHDAY_SCHOOL;
                model.setContent(content);
                model.setPicture(picture);
                model.setStatus(AppConstant.APP_TRUE);
                birthdayList.add(model);
            }
        }
        return birthdayList;
    }

    private List<HomeClassResponse> getClass(List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        List<HomeClassResponse> classList = new ArrayList<>();
        infoEmployeeSchoolList.forEach(x -> {
            List<ExEmployeeClass> exEmployeeClassList = x.getExEmployeeClassList();
            exEmployeeClassList.forEach(y -> {
                MaClass maClass = y.getMaClass();
                if (maClass.isDelActive()) {
                    HomeClassResponse model = new HomeClassResponse();
                    model.setIdClass(maClass.getId());
                    model.setClassName(maClass.getClassName());
                    classList.add(model);
                }
            });
        });
        return classList;
    }

    private String getLinkFacebook(Long idClass) {
        String linkFacebook = "";
        if (idClass != 0) {
            MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow(() -> new NotFoundException("not found maClass by id"));
            List<Media> mediaList = maClass.getMediaList().stream().filter(x -> x.isMediaActive() && x.getMediaType().equals(AppConstant.TYPE_FACEBOOK)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(mediaList)) {
                linkFacebook = mediaList.get(0).getLinkMedia();
            }
        }
        return linkFacebook;
    }

    private LinkResponse getLink() {
        LinkResponse model = new LinkResponse();
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        if (sysInfor != null) {
            model.setGuideLink(sysInfor.getGuideTeacherLink());
            model.setPolicyLink(sysInfor.getPolicyLink());
            model.setSupportLink(sysInfor.getSupportLink());
        }
        return model;
    }


}
