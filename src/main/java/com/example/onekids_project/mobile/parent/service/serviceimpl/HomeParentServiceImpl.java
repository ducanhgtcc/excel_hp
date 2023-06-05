package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.Album;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.entity.parent.MessageParent;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.*;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.parent.response.home.*;
import com.example.onekids_project.mobile.parent.response.schoolconfig.*;
import com.example.onekids_project.mobile.parent.service.servicecustom.FinanceKidsService;
import com.example.onekids_project.mobile.parent.service.servicecustom.HomeParentService;
import com.example.onekids_project.mobile.response.*;
import com.example.onekids_project.mobile.service.servicecustom.ChangeInforService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.response.system.SysInforResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.JwtParentResponse;
import com.example.onekids_project.service.servicecustom.SysInforService;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertConfigUtil;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.DataUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeParentServiceImpl implements HomeParentService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private AppIconParentRepository appIconParentRepository;

    @Autowired
    private AppIconParentAddRepository appIconParentAddRepository;

    @Autowired
    private EvaluateDateRepository evaluateDateRepository;

    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;

    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;

    @Autowired
    private EvaluatePeriodicRepository evaluatePeriodicRepository;

    @Autowired
    private MessageParentRepository messageParentRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private AbsentLetterRepository absentLetterRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

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
    private MaUserRepository maUserRepository;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private BirthdaySampleRepository birthdaySampleRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private SysInforRepository sysInforRepository;

    @Autowired
    private ExAlbumKidsRepository exAlbumKidsRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ChangeInforService changeInforService;

    @Autowired
    private FinanceKidsService financeKidsService;

    @Override
    public HomeParentResponse findHomeParent(UserPrincipal userPrincipal) {
        long idSchoolLogin = userPrincipal.getIdSchoolLogin();
        long idKidLogin = userPrincipal.getIdKidLogin();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(userPrincipal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in HomeParent"));
        Parent parent = maUser.getParent();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(parent.getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found Kids by id in home"));
        Long idSchoolInKid = kids.getIdSchool();
        HomeParentResponse homeParentResponse = new HomeParentResponse();
        List<HomeKidsInforResponse> homeKidsInforResponseList = new ArrayList<>();
        List<HomeIconResponse> homeIconResponseList = new ArrayList<>();
        HomeParentExtraResponse homeParentExtraResponse = new HomeParentExtraResponse();

        List<Kids> kidsList = parent.getKidsList().stream().filter(x -> x.isDelActive() && x.isActivated()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(kidsList)) {
            return null;
        }
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchoolInKid).orElseThrow();
        homeParentExtraResponse.setShowWalletParent(school.getSchoolConfig().isShowWalletParent());
        homeParentExtraResponse.setShowOnecamPlus(school.getSchoolConfig().isShowOnecamPlus());
        homeParentResponse.setSchoolName(school.getSchoolName());
        homeParentResponse.setIdKidLogin(idKidLogin);
        for (Kids x : kidsList) {
            HomeKidsInforResponse kidsInforResponse = new HomeKidsInforResponse();
            kidsInforResponse.setIdKid(x.getId());
            kidsInforResponse.setFullName(x.getFullName());
            kidsInforResponse.setNickName(StringUtils.isNotBlank(x.getNickName()) ? x.getNickName() : "");
            if (x.getAvatarKid() == null) {
                kidsInforResponse.setAvatar(AvatarDefaultConstant.AVATAR_KIDS);
            } else {
                kidsInforResponse.setAvatar(x.getAvatarKid());
            }
            kidsInforResponse.setClassName(x.getMaClass().getClassName());
            homeKidsInforResponseList.add(kidsInforResponse);
        }

        AppIconParent appIconParent = appIconParentRepository.findBySchoolId(idSchoolLogin).orElseThrow(() -> new NotFoundException("not found appIconParent by id"));
        AppIconParentAdd appIconParentAdd = appIconParentAddRepository.findByIdSchoolAndKidId(idSchoolLogin, idKidLogin).orElseThrow(() -> new NotFoundException("not found AppIconParentAdd by id"));

        HomeIconResponse model1 = new HomeIconResponse();
        model1.setKey(AppIconName.MESSAGE_KEY);
        model1.setValue(AppIconName.MESSAGE);
        if (appIconParent.isMessage()) {
            model1.setClickStatus(appIconParentAdd.isMessage());
            if (appIconParentAdd.isMessage()) {
                int newNotifi = getMessageParentNewNumber(idSchoolLogin, idKidLogin);
                model1.setNewNotifiNumber(newNotifi);
            }
        } else {
            model1.setClickStatus(AppConstant.APP_FALSE);
        }
        model1.setTextLock(appIconParent.getMessageLock());
        model1.setShowStatus(appIconParent.isMessageShow() && appIconParentAdd.isMessageShow());
        homeIconResponseList.add(model1);

        HomeIconResponse model2 = new HomeIconResponse();
        model2.setKey(AppIconName.MEDICINE_KEY);
        model2.setValue(AppIconName.MEDICINE);
        if (appIconParent.isMedicine()) {
            model2.setClickStatus(appIconParentAdd.isMedicine());
            if (appIconParentAdd.isMedicine()) {
                int newNotifi = getMedicineNewNumber(idSchoolLogin, idKidLogin);
                model2.setNewNotifiNumber(newNotifi);
            }
        } else {
            model2.setClickStatus(AppConstant.APP_FALSE);
        }
        model2.setTextLock(appIconParent.getMedicineLock());
        model2.setShowStatus(appIconParent.isMedicineShow() && appIconParentAdd.isMedicineShow());
        homeIconResponseList.add(model2);

        HomeIconResponse model3 = new HomeIconResponse();
        model3.setKey(AppIconName.ABSENT_KEY);
        model3.setValue(AppIconName.ABSENT);
        if (appIconParent.isAbsent()) {
            model3.setClickStatus(appIconParentAdd.isAbsent());
            if (appIconParentAdd.isAbsent()) {
                model3.setNewNotifiNumber(0);
                int newNotifi = getAbsentLetterNewNumber(idSchoolLogin, idKidLogin);
                model3.setNewNotifiNumber(newNotifi);
            }
        } else {
            model3.setClickStatus(AppConstant.APP_FALSE);
        }
        model3.setTextLock(appIconParent.getAbsentLock());
        model3.setShowStatus(appIconParent.isAbsentShow() && appIconParentAdd.isAbsentShow());
        homeIconResponseList.add(model3);

        HomeIconResponse model4 = new HomeIconResponse();
        model4.setKey(AppIconName.ALBUM_KEY);
        model4.setValue(AppIconName.ALBUM);
        if (appIconParent.isAlbum()) {
            model4.setClickStatus(appIconParentAdd.isAlbum());
            if (appIconParentAdd.isAlbum()) {
                //thêm ảnh mới cho album
                int newAlbum = this.getAlbumNewNumber(idKidLogin);
                model4.setNewNotifiNumber(newAlbum);
            }
        } else {
            model4.setClickStatus(AppConstant.APP_FALSE);
        }
        model4.setTextLock(appIconParent.getAlbumLock());
        model4.setShowStatus(appIconParent.isAlbumShow() && appIconParentAdd.isAlbumShow());
        homeIconResponseList.add(model4);

        HomeIconResponse model5 = new HomeIconResponse();
        model5.setKey(AppIconName.EVALUATE_KEY);
        model5.setValue(AppIconName.EVALUATE);
        if (appIconParent.isEvaluate()) {
            model5.setClickStatus(appIconParentAdd.isEvaluate());
            if (appIconParentAdd.isEvaluate()) {
                List<EvaluateDate> evaluateDateList = new ArrayList<>();
                List<EvaluateWeek> evaluateWeekList = new ArrayList<>();
                List<EvaluateMonth> evaluateMonthList = new ArrayList<>();
                List<EvaluatePeriodic> evaluatePeriodicList = new ArrayList<>();
                if (userPrincipal.getSchoolConfig().isEvaluateDate()) {
                    evaluateDateList = getEvaluateDate(idSchoolLogin, idKidLogin);
                }
                if (userPrincipal.getSchoolConfig().isEvaluateWeek()) {
                    evaluateWeekList = getEvaluateWeek(idSchoolLogin, idKidLogin);
                }
                if (userPrincipal.getSchoolConfig().isEvaluateMonth()) {
                    evaluateMonthList = getEvaluateMonth(idSchoolLogin, idKidLogin);
                }
                if (userPrincipal.getSchoolConfig().isEvaluatePeriod()) {
                    evaluatePeriodicList = getEvaluatePeriodic(idSchoolLogin, idKidLogin);
                }
                int newTotal = getTotalNotifi(evaluateDateList, evaluateWeekList, evaluateMonthList, evaluatePeriodicList);
                model5.setNewNotifiNumber(newTotal);
            }
        } else {
            model5.setClickStatus(AppConstant.APP_FALSE);
        }
        model5.setTextLock(appIconParent.getEvaluateLock());
        model5.setShowStatus(appIconParent.isEvaluateShow() && appIconParentAdd.isEvaluateShow());
        homeIconResponseList.add(model5);

        HomeIconResponse model6 = new HomeIconResponse();
        model6.setKey(AppIconName.ATTENDANCE_KEY);
        model6.setValue(AppIconName.ATTENDANCE);
        if (appIconParent.isAttendance()) {
            model6.setClickStatus(appIconParentAdd.isAttendance());
            if (appIconParentAdd.isAttendance()) {
                model6.setNewNotifiNumber(0);
            }
        } else {
            model6.setClickStatus(AppConstant.APP_FALSE);
        }
        model6.setTextLock(appIconParent.getAttendanceLock());
        model6.setShowStatus(appIconParent.isAttendanceShow() && appIconParentAdd.isAttendanceShow());
        homeIconResponseList.add(model6);

        HomeIconResponse model7 = new HomeIconResponse();
        model7.setKey(AppIconName.STUDENT_FEES_KEY);
        model7.setValue(AppIconName.STUDENT_FEES);
        if (appIconParent.isStudentFees()) {
            model7.setClickStatus(appIconParentAdd.isStudentFees());
            if (appIconParentAdd.isStudentFees()) {
                model7.setNewNotifiNumber(financeKidsService.getNumberOrderKidsNoCompleteYear(idKidLogin, LocalDate.now().getYear()));
            }
        } else {
            model7.setClickStatus(AppConstant.APP_FALSE);
        }
        model7.setTextLock(appIconParent.getStudentFeesLock());
        model7.setShowStatus(appIconParent.isStudentFeesShow() && appIconParentAdd.isStudentFeesShow());
        homeIconResponseList.add(model7);

        HomeIconResponse model8 = new HomeIconResponse();
        model8.setKey(AppIconName.LEARN_KEY);
        model8.setValue(AppIconName.LEARN);
        if (appIconParent.isLearn()) {
            model8.setClickStatus(appIconParentAdd.isLearn());
            if (appIconParentAdd.isLearn()) {
                model8.setNewNotifiNumber(0);
            }
        } else {
            model8.setClickStatus(AppConstant.APP_FALSE);
        }
        model8.setTextLock(appIconParent.getLearnLock());
        model8.setShowStatus(appIconParent.isLearnShow() && appIconParentAdd.isLearnShow());
        homeIconResponseList.add(model8);

        HomeIconResponse model9 = new HomeIconResponse();
        model9.setKey(AppIconName.MENU_KEY);
        model9.setValue(AppIconName.MENU);
        if (appIconParent.isMenu()) {
            model9.setClickStatus(appIconParentAdd.isMenu());
            if (appIconParentAdd.isMenu()) {
                model9.setNewNotifiNumber(0);
            }
        } else {
            model9.setClickStatus(AppConstant.APP_FALSE);
        }
        model9.setTextLock(appIconParent.getMenuLock());
        model9.setShowStatus(appIconParent.isMenuShow() && appIconParentAdd.isMenuShow());
        homeIconResponseList.add(model9);

        HomeIconResponse model10 = new HomeIconResponse();
        model10.setKey(AppIconName.VIDEO_KEY);
        model10.setValue(AppIconName.VIDEO);
        if (appIconParent.isVideo()) {
            model10.setClickStatus(appIconParentAdd.isVideo());
            if (appIconParentAdd.isVideo()) {
                model10.setNewNotifiNumber(0);
            }
        } else {
            model10.setClickStatus(AppConstant.APP_FALSE);
        }
        model10.setTextLock(appIconParent.getVideoLock());
        model10.setShowStatus(appIconParent.isVideoShow() && appIconParentAdd.isVideoShow());
        homeIconResponseList.add(model10);

        HomeIconResponse model11 = new HomeIconResponse();
        model11.setKey(AppIconName.CAMERA_KEY);
        model11.setValue(AppIconName.CAMERA);
        if (appIconParent.isCamera()) {
            model11.setClickStatus(appIconParentAdd.isCamera());
            if (appIconParentAdd.isCamera()) {
                model11.setNewNotifiNumber(0);
            }
        } else {
            model11.setClickStatus(AppConstant.APP_FALSE);
        }
        model11.setTextLock(appIconParent.getCameraLock());
        model11.setShowStatus(appIconParent.isCameraShow() && appIconParentAdd.isCameraShow());
        homeIconResponseList.add(model11);

        HomeIconResponse model12 = new HomeIconResponse();
        model12.setKey(AppIconName.KIDS_INFO_KEY);
        model12.setValue(AppIconName.KIDS_INFO);
        if (appIconParent.isKidsInfo()) {
            model12.setClickStatus(appIconParentAdd.isKidsInfo());
            if (appIconParentAdd.isKidsInfo()) {
                model12.setNewNotifiNumber(0);
            }
        } else {
            model12.setClickStatus(AppConstant.APP_FALSE);
        }
        model12.setTextLock(appIconParent.getKidsInfoLock());
        model12.setShowStatus(appIconParent.isKidsInfoShow() && appIconParentAdd.isKidsInfoShow());
        homeIconResponseList.add(model12);

        HomeIconResponse model13 = new HomeIconResponse();
        model13.setKey(AppIconName.UTILITY_KEY);
        model13.setValue(AppIconName.UTILITY);
        if (appIconParent.isUtility()) {
            model13.setClickStatus(appIconParentAdd.isUtility());
            if (appIconParentAdd.isUtility()) {
                model13.setNewNotifiNumber(0);
            }
        } else {
            model13.setClickStatus(AppConstant.APP_FALSE);
        }
        model13.setTextLock(appIconParent.getUtilityLock());
        model13.setShowStatus(appIconParent.isUtilityShow() && appIconParentAdd.isUtilityShow());
        homeIconResponseList.add(model13);

        HomeIconResponse model14 = new HomeIconResponse();
        model14.setKey(AppIconName.FACEBOOK_KEY);
        model14.setValue(AppIconName.FACEBOOK);
        if (appIconParent.isFacebook()) {
            model14.setClickStatus(appIconParentAdd.isFacebook());
            if (appIconParentAdd.isFacebook()) {
                model14.setNewNotifiNumber(0);
            }
        } else {
            model14.setClickStatus(AppConstant.APP_FALSE);
        }
        model14.setTextLock(appIconParent.getFacebookLock());
        model14.setShowStatus(appIconParent.isFacebookShow() && appIconParentAdd.isFacebookShow());
        homeIconResponseList.add(model14);

        HomeIconResponse model15 = new HomeIconResponse();
        model15.setKey(AppIconName.FEEDBACK_KEY);
        model15.setValue(AppIconName.FEEDBACK);
        if (appIconParent.isFeedback()) {
            model15.setClickStatus(appIconParentAdd.isFeedback());
            if (appIconParentAdd.isFeedback()) {
                int newNotifi = getFeedBackNewNumber(idSchoolLogin, idKidLogin);
                model15.setNewNotifiNumber(newNotifi);
            }
        } else {
            model15.setClickStatus(AppConstant.APP_FALSE);
        }
        model15.setTextLock(appIconParent.getFeedbackLock());
        model15.setShowStatus(appIconParent.isFeedbackShow() && appIconParentAdd.isFeedbackShow());
        homeIconResponseList.add(model15);

        HomeIconResponse model16 = new HomeIconResponse();
        model16.setKey(AppIconName.NEWS_KEY);
        model16.setValue(AppIconName.NEWS_NAME);
        if (appIconParent.isNews()) {
            model16.setClickStatus(appIconParentAdd.isNews());
            if (appIconParentAdd.isNews()) {
                model16.setNewNotifiNumber(0);
            }
        } else {
            model16.setClickStatus(AppConstant.APP_FALSE);
        }
        model16.setTextLock(appIconParent.getNewsLock());
        model16.setShowStatus(appIconParent.isNewsShow() && appIconParentAdd.isNewsShow());
        homeIconResponseList.add(model16);

        homeParentResponse.setHomeKidsInforResponseList(homeKidsInforResponseList);
        homeParentResponse.setHomeIconParentResponseList(homeIconResponseList);
        homeParentResponse.setHomeParentExtra(homeParentExtraResponse);
        this.setBirthDay(homeParentResponse, maUser, userPrincipal);
        this.setLinkFacebook(homeParentResponse, kids.getMaClass().getId());
        this.setLink(homeParentResponse);
        return homeParentResponse;
    }

    @Override
    public List<PhoneBookOfParentResponse> findPhoneOfParent(UserPrincipal principal) {
        List<PhoneBookOfParentResponse> phoneBookOfParentResponseList = new ArrayList<>();
        //set phone of school
        this.setPhoneSupportOnekids(phoneBookOfParentResponseList);
        this.setPhoneSchool(principal, phoneBookOfParentResponseList);
        this.setPhoneTeacher(principal, phoneBookOfParentResponseList);
        return phoneBookOfParentResponseList;
    }

    @Override
    public ChangeTokenResponse changeKids(UserPrincipal principal, Long idKid) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id in change kid"));
        Parent parent = maUser.getParent();
        Long oldIdKid = parent.getIdKidLogin();
        Long newIdKid = idKid;
        Kids oldKid = kidsRepository.findById(oldIdKid).orElseThrow(() -> new NotFoundException("not found oldkid by id"));
        Kids newKid = kidsRepository.findById(newIdKid).orElseThrow(() -> new NotFoundException("not found newkid by id"));
        ChangeTokenResponse model = new ChangeTokenResponse();
        if (CollectionUtils.isEmpty(parent.getKidsList())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phụ huynh không có học sinh nào");
        }
        List<Long> idKidList = parent.getKidsList().stream().map(Kids::getId).collect(Collectors.toList());
        long count = idKidList.stream().filter(x -> x.equals(idKid)).count();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id trẻ không hợp lệ");
        }
        model.setSameSchool(oldKid.getIdSchool().equals(newKid.getIdSchool()) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        parent.setIdKidLogin(idKid);
        parentRepository.save(parent);
        String token = changeInforService.findNewToken(principal);
        model.setToken(token);
        return model;
    }

    @Override
    public JwtParentResponse findHomeFirstParent(UserPrincipal principal) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id"));
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found schoolInfo by id"));
        JwtParentResponse jwtParentResponse = new JwtParentResponse();
        SchoolConfigParent schoolConfigParent = new SchoolConfigParent();
        Parent parent = maUser.getParent();
        if (parent == null) {
            return null;
        }
        List<Kids> kidsList = parent.getKidsList();
        if (CollectionUtils.isEmpty(kidsList)) {
            return null;
        }

        Long idKidLogin = principal.getIdKidLogin();
        if (idKidLogin == null) {
            return null;
        }
        Kids kid = kidsRepository.findByIdAndDelActiveTrue(idKidLogin).orElseThrow(() -> new NotFoundException("not found kid by id"));
        LocalDate dateStart = kid.getDateStart();
        SchoolConfigResponse schoolConfigResponse = principal.getSchoolConfig();
        jwtParentResponse.setWeekList(DataUtils.getWeekList());
        jwtParentResponse.setMonthList(DataUtils.getMonthList());
        jwtParentResponse.setQuality(principal.getSysConfig().getQualityPicture());
        jwtParentResponse.setWidth(principal.getSysConfig().getWidthPicture());
        EatConfig eatConfig = modelMapper.map(schoolConfigResponse, EatConfig.class);
        LearnConfig learnConfig = ConvertConfigUtil.setLearnConfig(schoolConfigResponse);
        EvaluateConfig evaluateConfig = ConvertConfigUtil.setEvaluateConfig(schoolConfigResponse);
        AbsentConfig absentConfig = ConvertConfigUtil.setAbsentConfig(schoolConfigResponse);
        schoolConfigParent.setLearnConfig(learnConfig);
        schoolConfigParent.setEatConfig(eatConfig);
        schoolConfigParent.setEvaluateConfig(evaluateConfig);
        schoolConfigParent.setAbsentConfig(absentConfig);
        schoolConfigParent.setShowAttendanceConfig(modelMapper.map(schoolInfo, ShowAttendanceConfig.class));
        jwtParentResponse.setSchoolConfig(schoolConfigParent);
        return jwtParentResponse;
    }

    @Override
    public ListMobileNotifiParentResponse findNotifiKidsForMobile(UserPrincipal principal, Pageable pageable) {
        CommonValidate.checkDataParent(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idKid = principal.getIdKidLogin();
        List<Receivers> receiversList = receiversRepository.findNotifiKidsForMobile(principal.getId(), idSchool, idKid, pageable);
        ListMobileNotifiParentResponse listMobileNotifiParentResponse = new ListMobileNotifiParentResponse();
        List<MobileNotifiParentResponse> receiversResponseList = new ArrayList<>();
        receiversList.forEach(x -> {
            MobileNotifiParentResponse model = new MobileNotifiParentResponse();
            model.setId(x.getId());
            if (x.getAppSend().getIdCreated().equals(SystemConstant.ID_SYSTEM)) {
                model.setAvatar(AvatarDefaultConstant.AVATAR_SYSTEM);
            } else {
                MaUser maUser = maUserRepository.findById(x.getAppSend().getIdCreated()).orElseThrow(() -> new NoSuchElementException(ErrorsConstant.NOT_FOUND_USER));
                String appType = maUser.getAppType();
                if (appType.equals(AppTypeConstant.SUPPER_SCHOOL) || appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.TEACHER)) {
                    if (appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.SUPPER_SCHOOL)) {
                        model.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                    } else if (appType.equals(AppTypeConstant.TEACHER)) {
                        model.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
                    }
                } else {
                    model.setAvatar(AvatarDefaultConstant.AVATAR_SYSTEM);
                }
            }
            model.setTitle(x.getAppSend().getSendTitle());
            String content = x.getAppSend().getSendContent();
            model.setContent(content);
//            if (StringUtils.isBlank(content) || content.length() <= 50) {
//                model.setContent(content);
//            } else {
//                model.setContent(content.substring(0, 50));
//            }
            List<UrlFileAppSend> urlFileAppSendList = x.getAppSend().getUrlFileAppSendList();
            int pictureNumber = (int) urlFileAppSendList.stream().filter(y -> StringUtils.isNotBlank(y.getAttachPicture())).count();
            int fileNumber = (int) urlFileAppSendList.stream().filter(y -> StringUtils.isNotBlank(y.getAttachFile())).count();
            model.setPictureNumber(pictureNumber);
            model.setFileNumber(fileNumber);
            model.setCreatedDate(x.getCreatedDate());
            model.setSeen(x.isUserUnread());
            receiversResponseList.add(model);
        });
        Long countAll = receiversRepository.getCountNotifi(idSchool, idKid);
        long pageSizeCurrent = pageable.getOffset() + pageable.getPageSize();
        boolean checkLastPage = pageSizeCurrent > countAll;

        listMobileNotifiParentResponse.setNotifilList(receiversResponseList);
        listMobileNotifiParentResponse.setLastPage(checkLastPage);
        return listMobileNotifiParentResponse;
    }

    @Override
    public MobileNotifiDetailParentResponse findNotifiByIdForMobile(Long id) {
        Receivers receivers = receiversRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found notify detail by id"));
        MobileNotifiDetailParentResponse model = new MobileNotifiDetailParentResponse();
        List<String> urlPictureList = new ArrayList<>();
        List<AttachFileMobileResponse> mobileFileList = new ArrayList<>();
        model.setTitle(receivers.getAppSend().getSendTitle());
        model.setContent(receivers.getAppSend().getSendContent());
        model.setCreatedDate(receivers.getCreatedDate());

        List<UrlFileAppSend> urlFileAppSendList = receivers.getAppSend().getUrlFileAppSendList();
        urlFileAppSendList.forEach(x -> {
            String urlPicture = x.getAttachPicture();
            String urlFile = x.getAttachFile();
            if (StringUtils.isNotBlank(urlPicture)) {
                urlPictureList.add(urlPicture);
            }
            if (StringUtils.isNotBlank(urlFile)) {
                AttachFileMobileResponse mobileFile = new AttachFileMobileResponse();
                mobileFile.setName(x.getName());
                mobileFile.setUrl(urlFile);
                mobileFileList.add(mobileFile);
            }
        });
        LocalDateTime time = LocalDateTime.now();
        model.setPictureList(urlPictureList);
        model.setFileList(mobileFileList);
        receivers.setUserUnread(AppConstant.APP_TRUE);
        receivers.setTimeRead(time);
        receiversRepository.save(receivers);
        return model;
    }

    @Override
    public long findNotifiUnRead(UserPrincipal principal) {
        long countUnread = receiversRepository.findNotifiKidsForUnReadMobile(principal.getIdSchoolLogin(), principal.getIdKidLogin());
        return countUnread;
    }

//    private String getAvatarMobile(Long idSchool, Long id, String appType) {
//        String avatar = "";
//        if (AppTypeConstant.SYSTEM.equals(appType)) {
//            avatar = AvatarDefaultConstant.AVATAR_SYSTEM;
//        } else if (AppTypeConstant.SCHOOL.equals(appType)) {
//            MaUser maUser = maUserRepository.findById(id).orElseThrow(() -> new NoSuchElementException(ErrorsConstant.NOT_FOUND_USER));
//            avatar= ConvertData.getAvatarUser(maUser);
//            List<InfoEmployeeSchool> infoEmployeeSchoolList = maUser.getEmployee().getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId() == idSchool).collect(Collectors.toList());
//            if (CollectionUtils.isEmpty(infoEmployeeSchoolList) || infoEmployeeSchoolList.size() > 1) {
//                throw new NotFoundException("not found idUser create appsend");
//            }
//            avatar = infoEmployeeSchoolList.get(0).getAvatar();
//            if (StringUtils.isBlank(avatar)) {
//                avatar = AvatarDefaultConstant.AVATAR_SCHOOL;
//            }
//
//        } else if (AppTypeConstant.TEACHER.equals(appType)) {
//            MaUser maUser = maUserRepository.findById(id).orElseThrow(() -> new NotFoundException("HomeParentService: not found maUser by id"));
//            List<InfoEmployeeSchool> infoEmployeeSchoolList = maUser.getEmployee().getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId() == idSchool).collect(Collectors.toList());
//            if (CollectionUtils.isEmpty(infoEmployeeSchoolList) || infoEmployeeSchoolList.size() > 1) {
//                throw new NotFoundException("not found idUser create appsend");
//            }
//            avatar = infoEmployeeSchoolList.get(0).getAvatar();
//            if (StringUtils.isBlank(avatar)) {
//                avatar = AvatarDefaultConstant.AVATAR_TEACHER;
//            }
//        }
//        return avatar;
//    }

    /**
     * tìm kiếm số điện thoại của trường học sinh đang đăng nhập
     *
     * @param userPrincipal
     * @param dataList
     */
    private void setPhoneSchool(UserPrincipal userPrincipal, List<PhoneBookOfParentResponse> dataList) {
        Long idSchoolLogin = userPrincipal.getIdSchoolLogin();
        if (idSchoolLogin == null) {
            return;
        }

        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(idSchoolLogin, AppConstant.APP_TRUE);
        if (schoolOptional.isEmpty()) {
            return;
        }
        School school = schoolOptional.get();

        String phone = school.getSchoolPhone();
        String phone1 = school.getContactPhone1();
        String phone2 = school.getContactPhone2();
        String phone3 = school.getContactPhone3();

        if (StringUtils.isNotBlank(phone)) {
            PhoneBookOfParentResponse phoneBookOfParentResponse = new PhoneBookOfParentResponse();
            phoneBookOfParentResponse.setPhone(phone);
            phoneBookOfParentResponse.setName(AppConstant.SCHOOL_NAME);
//            if (StringUtils.isBlank(school.getSchoolAvatar())) {
//                phoneBookOfParentResponse.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
//            } else {
//                phoneBookOfParentResponse.setAvatar(school.getSchoolAvatar());
//            }
            phoneBookOfParentResponse.setAvatar(AvatarUtils.getAvatarSchool(school));
            phoneBookOfParentResponse.setType(AppConstant.PHONE_TYPE_SCHOOL);
            dataList.add(phoneBookOfParentResponse);
        }

        if (StringUtils.isNotBlank(phone1)) {
            PhoneBookOfParentResponse phoneBookOfParentResponse = new PhoneBookOfParentResponse();
            phoneBookOfParentResponse.setPhone(phone1);
            phoneBookOfParentResponse.setName(school.getContactName1());
            phoneBookOfParentResponse.setAvatar(AvatarUtils.getAvatarSchool(school));
            phoneBookOfParentResponse.setType(AppConstant.PHONE_TYPE_SCHOOL);
            dataList.add(phoneBookOfParentResponse);
        }

        if (StringUtils.isNotBlank(phone2)) {
            PhoneBookOfParentResponse phoneBookOfParentResponse = new PhoneBookOfParentResponse();
            phoneBookOfParentResponse.setPhone(phone2);
            phoneBookOfParentResponse.setName(school.getContactName2());
            phoneBookOfParentResponse.setAvatar(AvatarUtils.getAvatarSchool(school));
            phoneBookOfParentResponse.setType(AppConstant.PHONE_TYPE_SCHOOL);
            dataList.add(phoneBookOfParentResponse);
        }

        if (StringUtils.isNotBlank(phone3)) {
            PhoneBookOfParentResponse phoneBookOfParentResponse = new PhoneBookOfParentResponse();
            phoneBookOfParentResponse.setPhone(phone3);
            phoneBookOfParentResponse.setName(school.getContactName3());
            phoneBookOfParentResponse.setAvatar(AvatarUtils.getAvatarSchool(school));
            phoneBookOfParentResponse.setType(AppConstant.PHONE_TYPE_SCHOOL);
            dataList.add(phoneBookOfParentResponse);
        }

    }

    /**
     * lấy sdt của giáo viên dạy học sinh đang đăng nhập
     *
     * @param userPrincipal
     * @param dataList
     */
    private void setPhoneTeacher(UserPrincipal userPrincipal, List<PhoneBookOfParentResponse> dataList) {
        Long idKid = userPrincipal.getIdKidLogin();
        Long idSchool = userPrincipal.getIdSchoolLogin();
        Kids kids = kidsRepository.findById(idKid).orElseThrow();
        Long idClassOfKid = kids.getMaClass().getId();
        List<ExEmployeeClass> exEmployeeClassList = exEmployeeClassRepository.findByMaClassIdAndDelActiveTrue(idClassOfKid);
        if (CollectionUtils.isEmpty(exEmployeeClassList)) {
            return;
        }
        List<InfoEmployeeSchool> infoEmployeeSchoolList = exEmployeeClassList.stream().map(ExEmployeeClass::getInfoEmployeeSchool).collect(Collectors.toList());
        infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(x -> x.isDelActive() && x.getEmployee() != null && x.getAppType().equals(AppTypeConstant.TEACHER)).collect(Collectors.toList());
        infoEmployeeSchoolList.forEach(x -> {
            MaUser maUser = x.getEmployee().getMaUser();
            PhoneBookOfParentResponse model = new PhoneBookOfParentResponse();
            //cho phép xem sdt giáo viên
            if (userPrincipal.getSchoolConfig().isEmployeePhone()) {
                model.setPhone(maUser.getPhone());
            } else {
                //không cho phép xem sdt giáo viên
                model.setPhone(MobileConstant.HIDDEN_PHONE);
            }
            model.setName(maUser.getFullName());
            model.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
            model.setType(AppConstant.PHONE_TYPE_TEACHER);
            dataList.add(model);
        });

    }

    /**
     * lấy số điện thoại của hỗ trợ onekids
     *
     * @param dataList
     */
    private void setPhoneSupportOnekids(List<PhoneBookOfParentResponse> dataList) {
        SysInforResponse sysInforResponse = sysInforService.getFirstSupportOnekids();
        String phone1 = sysInforResponse.getPhone1();
        String phone2 = sysInforResponse.getPhone2();
        String phone3 = sysInforResponse.getPhone3();

        if (StringUtils.isNotBlank(phone1)) {
            PhoneBookOfParentResponse phoneBookOfParentResponse = new PhoneBookOfParentResponse();
            phoneBookOfParentResponse.setPhone(phone1);
            phoneBookOfParentResponse.setName(sysInforResponse.getNamePhone1());
            phoneBookOfParentResponse.setAvatar(AvatarDefaultConstant.AVATAR_SYSTEM);
            phoneBookOfParentResponse.setType(AppConstant.PHONE_TYPE_SUPPORT);
            dataList.add(phoneBookOfParentResponse);
        }
        if (StringUtils.isNotBlank(phone2)) {
            PhoneBookOfParentResponse phoneBookOfParentResponse = new PhoneBookOfParentResponse();
            phoneBookOfParentResponse.setPhone(phone2);
            phoneBookOfParentResponse.setName(sysInforResponse.getNamePhone2());
            phoneBookOfParentResponse.setAvatar(AvatarDefaultConstant.AVATAR_SYSTEM);
            phoneBookOfParentResponse.setType(AppConstant.PHONE_TYPE_SUPPORT);
            dataList.add(phoneBookOfParentResponse);
        }
        if (StringUtils.isNotBlank(phone3)) {
            PhoneBookOfParentResponse phoneBookOfParentResponse = new PhoneBookOfParentResponse();
            phoneBookOfParentResponse.setPhone(phone3);
            phoneBookOfParentResponse.setName(sysInforResponse.getNamePhone3());
            phoneBookOfParentResponse.setAvatar(AvatarDefaultConstant.AVATAR_SYSTEM);
            phoneBookOfParentResponse.setType(AppConstant.PHONE_TYPE_SUPPORT);
            dataList.add(phoneBookOfParentResponse);
        }

    }


    /**
     * tìm kiếm đánh giá các ngày của một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    private List<EvaluateDate> getEvaluateDate(Long idSchool, Long idKid) {
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findEvaluateDateKidMobile(idSchool, idKid);
        return evaluateDateList;
    }

    /**
     * tìm kiếm đánh giá các tuần của một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    private List<EvaluateWeek> getEvaluateWeek(Long idSchool, Long idKid) {
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findEvaluateWeekMobile(idSchool, idKid);
        return evaluateWeekList;
    }

    /**
     * tìm kiếm đánh giá các tháng của một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    private List<EvaluateMonth> getEvaluateMonth(Long idSchool, Long idKid) {
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findEvaluateMonthMobile(idSchool, idKid);
        return evaluateMonthList;
    }

    /**
     * tìm kiếm đánh giá các định kỳ của một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    private List<EvaluatePeriodic> getEvaluatePeriodic(Long idSchool, Long idKid) {
        List<EvaluatePeriodic> evaluatePeriodicList = evaluatePeriodicRepository.findEvaluatePeriodicMobile(idSchool, idKid);
        return evaluatePeriodicList;
    }

    private int getNewNotifiDate(List<EvaluateDate> evaluateDateList) {
        int newNotifi = 0;
        if (CollectionUtils.isEmpty(evaluateDateList)) {
            return newNotifi;
        }
        newNotifi = (int) evaluateDateList.stream().filter(x -> x.isDelActive() && x.isApproved() && !x.isParentRead() && (StringUtils.isNotBlank(x.getLearnContent()) || StringUtils.isNotBlank(x.getEatContent()) || StringUtils.isNotBlank(x.getSleepContent()) || StringUtils.isNotBlank(x.getSanitaryContent()) || StringUtils.isNotBlank(x.getHealtContent()) || StringUtils.isNotBlank(x.getCommonContent()) || !CollectionUtils.isEmpty(x.getEvaluateAttachFileList()))).count();
        return newNotifi;
    }

    private int getNewNotifiWeek(List<EvaluateWeek> evaluateWeekList) {
        int newNotifi = 0;
        if (CollectionUtils.isEmpty(evaluateWeekList)) {
            return newNotifi;
        }
        newNotifi = (int) evaluateWeekList.stream().filter(x -> x.isDelActive() && x.isApproved() && !x.isParentRead() && (StringUtils.isNotBlank(x.getContent()) || !CollectionUtils.isEmpty(x.getEvaluateWeekFileList()))).count();
        return newNotifi;
    }

    private int getNewNotifiMonth(List<EvaluateMonth> evaluateMonthList) {
        int newNotifi = 0;
        if (CollectionUtils.isEmpty(evaluateMonthList)) {
            return newNotifi;
        }
        newNotifi = (int) evaluateMonthList.stream().filter(x -> x.isDelActive() && x.isApproved() && !x.isParentRead() && (StringUtils.isNotBlank(x.getContent()) || !CollectionUtils.isEmpty(x.getEvaluateMonthFileList()))).count();
        return newNotifi;
    }

    private int getNewNotifiPeriodic(List<EvaluatePeriodic> evaluatePeriodicList) {
        int newNotifi = 0;
        if (CollectionUtils.isEmpty(evaluatePeriodicList)) {
            return newNotifi;
        }
        newNotifi = (int) evaluatePeriodicList.stream().filter(x -> x.isDelActive() && x.isApproved() && !x.isParentRead() && (StringUtils.isNotBlank(x.getContent()) || !CollectionUtils.isEmpty(x.getEvaluatePeriodicFileList()))).count();
        return newNotifi;
    }

    /**
     * tổng số thông báo mới cho đánh giá của một học sinh
     *
     * @param evaluateDateList
     * @param evaluateWeekList
     * @param evaluateMonthList
     * @param evaluatePeriodicList
     * @return
     */
    private int getTotalNotifi(List<EvaluateDate> evaluateDateList, List<EvaluateWeek> evaluateWeekList, List<EvaluateMonth> evaluateMonthList, List<EvaluatePeriodic> evaluatePeriodicList) {
        int total = getNewNotifiDate(evaluateDateList) + getNewNotifiWeek(evaluateWeekList) + getNewNotifiMonth(evaluateMonthList) + getNewNotifiPeriodic(evaluatePeriodicList);
        return total;
    }

    /**
     * lấy các lời nhắn chưa đọc của một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    private int getMessageParentNewNumber(Long idSchool, Long idKid) {
        int newNumber = 0;
        List<MessageParent> messageParentList = messageParentRepository.findByKidsIdAndIdSchoolAndParentMessageDelFalseAndDelActiveTrue(idKid, idSchool);
        if (CollectionUtils.isEmpty(messageParentList)) {
            return newNumber;
        }
        newNumber = (int) messageParentList.stream().filter(x -> !x.isParentMessageDel() && x.isConfirmStatus() && !x.isParentUnread()).count();
        return newNumber;
    }

    /**
     * lấy các dặn thuốc chưa đọc của một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    private int getMedicineNewNumber(Long idSchool, Long idKid) {
        int newNumber = 0;
        List<Medicine> medicineList = medicineRepository.findByKidsIdAndIdSchoolAndDelActive(idKid, idSchool, AppConstant.APP_TRUE);
        if (CollectionUtils.isEmpty(medicineList)) {
            return newNumber;
        }
        newNumber = (int) medicineList.stream().filter(x -> !x.isParentMedicineDel() && x.isConfirmStatus() && !x.isParentUnread()).count();
        return newNumber;
    }

    /**
     * lấy các lời nhắn chưa đọc của một học sinh
     *
     * @param idKid
     * @return
     */
    private int getAbsentLetterNewNumber(Long idSchool, Long idKid) {
        int newNumber = 0;
        List<AbsentLetter> absentLetterList = absentLetterRepository.findByKidsIdAndIdSchoolAndDelActive(idKid, idSchool, AppConstant.APP_TRUE);
        if (CollectionUtils.isEmpty(absentLetterList)) {
            return newNumber;
        }
        newNumber = (int) absentLetterList.stream().filter(x -> !x.isParentAbsentDel() && x.isConfirmStatus() && !x.isParentUnread()).count();
        return newNumber;
    }

    /**
     * tìm kiếm số album mới
     *
     * @param idKid
     * @return
     */
    private int getAlbumNewNumber(Long idKid) {
        int newNumber = 0;
        List<ExAlbumKids> exAlbumKidsList = exAlbumKidsRepository.findByKidsIdAndStatusUnreadFalse(idKid);
        if (CollectionUtils.isEmpty(exAlbumKidsList)) {
            return newNumber;
        }
        for (ExAlbumKids x : exAlbumKidsList) {
            if (!x.isStatusUnread()) {
                Album album = albumRepository.findById(x.getAlbum().getId()).orElseThrow(() -> new NotFoundException("not found album by id in home"));
                long countPicutreApproved = album.getAlistPictureList().stream().filter(a -> a.getIsApproved()).count();
                if (countPicutreApproved > 0) {
                    newNumber++;
                }
            }
        }
        return newNumber;
    }

    /**
     * lấy các lời nhắn chưa đọc của một học sinh
     *
     * @param idSchool
     * @param idKid
     * @return
     */
    private int getFeedBackNewNumber(Long idSchool, Long idKid) {
        int newNumber = 0;
        List<FeedBack> feedBackList = feedBackRepository.findByIdKidAndIdSchoolAndDelActive(idKid, idSchool, AppConstant.APP_TRUE);
        if (CollectionUtils.isEmpty(feedBackList)) {
            return newNumber;
        }
        newNumber = (int) feedBackList.stream().filter(x -> x.isSchoolConfirmStatus() && !x.isParentUnread()).count();
        return newNumber;
    }

    /**
     * set birth day for nowdate
     *
     * @param model
     * @param maUser
     */
    private void setBirthDay(HomeParentResponse model, MaUser maUser, UserPrincipal principal) {
        //set birthday for kids
        List<BirthdayMobileResponse> birthdayMobileResponseList = new ArrayList<>();
        Parent parent = maUser.getParent();
        List<Kids> kidsList = parent.getKidsList().stream().filter(x -> x.isDelActive() && x.isActivated()).collect(Collectors.toList());
//        Kids kids = kidsRepository.findByIdAndDelActiveTrue(maUser.getParent().getIdKidLogin()).orElseThrow(() -> new NotFoundException("not found kids by id in home"));
        LocalDate nowDate = LocalDate.now();
        int date = nowDate.getDayOfMonth();
        int month = nowDate.getMonthValue();
        Optional<BirthdaySample> kidsSampleOptional = birthdaySampleRepository.findByIdSchoolAndBirthdayTypeAndActiveTrue(principal.getIdSchoolLogin(), SampleConstant.KIDS);
        Optional<BirthdaySample> parentSampleOptional = birthdaySampleRepository.findByIdSchoolAndBirthdayTypeAndActiveTrue(principal.getIdSchoolLogin(), SampleConstant.PARENT);
        kidsList.forEach(x -> {
            //sinh nhật học sinh
            if (kidsSampleOptional.isPresent() && x.getBirthDay().getDayOfMonth() == date && x.getBirthDay().getMonthValue() == month) {
                BirthdaySample birthdaySample = kidsSampleOptional.get();
                BirthdayMobileResponse birthDayKis = new BirthdayMobileResponse();
                String content = birthdaySample.getContent();
                content = content.replace(SampleConstant.NAME, x.getFullName());
                String picture = StringUtils.isNotBlank(birthdaySample.getUrlPicture()) ? birthdaySample.getUrlPicture() : PictureConstant.BIRTHDAY_SCHOOL;
                birthDayKis.setIdSchool(x.getIdSchool());
                birthDayKis.setContent(content);
                birthDayKis.setPicture(picture);
                birthdayMobileResponseList.add(birthDayKis);
            }
            //sinh nhật phụ huynh
            if (parentSampleOptional.isPresent() && ((x.getFatherBirthday() != null && x.getFatherBirthday().getDayOfMonth() == date && x.getFatherBirthday().getMonthValue() == month) || (x.getMotherBirthday() != null && x.getMotherBirthday().getDayOfMonth() == date && x.getMotherBirthday().getMonthValue() == month))) {
                BirthdaySample birthdaySample = parentSampleOptional.get();
                BirthdayMobileResponse birthDayKis = new BirthdayMobileResponse();
                String content = birthdaySample.getContent();
                content = content.replace(SampleConstant.NAME, "");
                String picture = StringUtils.isNotBlank(birthdaySample.getUrlPicture()) ? birthdaySample.getUrlPicture() : PictureConstant.BIRTHDAY_SCHOOL;
                birthDayKis.setIdSchool(x.getIdSchool());
                birthDayKis.setContent(content);
                birthDayKis.setPicture(picture);
                birthdayMobileResponseList.add(birthDayKis);
            }
        });
        model.setBirthDayList(birthdayMobileResponseList);
    }


    /**
     * set link facebook
     *
     * @param model
     * @param idClass
     */
    private void setLinkFacebook(HomeParentResponse model, Long idClass) {
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow(() -> new NotFoundException("not found maClass by id"));
        List<Media> mediaList = maClass.getMediaList().stream().filter(x -> x.isMediaActive() && x.getMediaType().equals(AppConstant.TYPE_FACEBOOK)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(mediaList)) {
            model.setLinkFacebook(mediaList.get(0).getLinkMedia());
        } else {
            model.setLinkFacebook("");
        }
    }

    /**
     * set link for parent
     *
     * @param homeParentResponse
     */
    private void setLink(HomeParentResponse homeParentResponse) {
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        LinkResponse linkResponse = new LinkResponse();
        linkResponse.setGuideLink(sysInfor.getGuideLink());
        linkResponse.setPolicyLink(sysInfor.getPolicyLink());
        linkResponse.setSupportLink(sysInfor.getSupportLink());
        homeParentResponse.setDataLink(linkResponse);
    }
}
