package com.example.onekids_project.commoncontroller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.cronjobs.AttendanceKidsCronjobs;
import com.example.onekids_project.cronjobs.CashbookCronjobs;
import com.example.onekids_project.cronjobs.EvaluateKidsCronjobs;
import com.example.onekids_project.entity.school.*;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.test.TableTest;
import com.example.onekids_project.entity.user.Api;
import com.example.onekids_project.entity.user.Role;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.finance.CycleMoneyService;
import com.example.onekids_project.service.servicecustom.finance.FnPackageService;
import com.example.onekids_project.service.servicecustom.onecam.OneCamConfigService;
import com.example.onekids_project.service.servicecustom.onecam.OneCamSettingService;
import com.example.onekids_project.service.serviceimpl.SchoolServiceImpl;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * date 2021-02-24 16:38
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/data/create")
public class CreateDataController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SchoolServiceImpl schoolServiceImpl;
    @Autowired
    private SchoolConfigRepository schoolConfigRepository;

    @Autowired
    private WalletParentRepository walletParentRepository;

    @Autowired
    private FnCashBookRepository fnCashBookRepository;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @Autowired
    private ConfigAttendanceEmployeeSchoolRepository configAttendanceEmployeeSchoolRepository;

    @Autowired
    private ConfigAttendanceEmployeeRepository configAttendanceEmployeeRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private ConfigNotifyPlusRepository configNotifyPlusRepository;
    @Autowired
    private ConfigNotifyTeacherRepository configNotifyTeacherRepository;
    @Autowired
    private ConfigNotifyParentRepository configNotifyParentRepository;
    @Autowired
    private ApiRepository apiRepository;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private DayOffClassRepository dayOffClassRepository;
    @Autowired
    private TableTestRepository tableTestRepository;
    @Autowired
    private GroupOutKidsRepository groupOutKidsRepository;
    @Autowired
    private GroupOutEmployeeRepository groupOutEmployeeRepository;
    @Autowired
    private InternalNotificationPlusRepository internalNotificationPlusRepository;
    @Autowired
    private NotificationManageRepository notificationManageRepository;
    @Autowired
    private NotificationManageDateRepository notificationManageDateRepository;
    @Autowired
    private AttendanceKidsCronjobs attendanceKidsCronjobs;
    @Autowired
    private EvaluateKidsCronjobs evaluateKidsCronjobs;
    @Autowired
    private OneCamNewsRepository oneCamNewsRepository;
    @Autowired
    private OneCamSettingService oneCamSettingService;
    @Autowired
    private OneCamConfigService oneCamConfigService;
    @Autowired
    private CycleMoneyService cycleMoneyService;
    @Autowired
    private FnPackageService fnPackageService;

    @Autowired
    private CashbookCronjobs cashbookCronjobs;

    /**
     * tạo bằng tay điểm danh nhận xét cho tất cả các trường
     */
    @RequestMapping(method = RequestMethod.POST, value = "/attendance-evaluate")
    public String createAttendanceAndEvaluate(@CurrentUser UserPrincipal principal) {
//        LocalDate date = LocalDate.now();
//        for (int i = 0; i <= 90; i++) {
//            attendanceKidsCronjobs.generateAttendanceKids(date.plusDays(i));
//            evaluateKidsCronjobs.generateEvaluateDateKids(date.plusDays(i));
//        }
        return "create attendance and evaluate success";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/wallet")
    public String createWallet() {
//        List<Parent> parentList = parentRepository.findAll();
//        parentList.forEach(parent -> {
//            List<Long> idKidList = parent.getKidsList().stream().map(Kids::getIdSchool).distinct().collect(Collectors.toList());
//            idKidList.forEach(b -> {
//                WalletParent walletParent = new WalletParent();
//                School school = schoolRepository.findById(b).orElseThrow();
//                Optional<WalletParent> walletParentOptional;
//                String code;
//                do {
//                    code = school.getSchoolCode().substring(2).concat("-").concat(GenerateCode.getNumberInput(6));
//                    walletParentOptional = walletParentRepository.findByCode(code);
//                } while (walletParentOptional.isPresent());
//                walletParent.setCode(code);
//                walletParent.setParent(parent);
//                walletParent.setIdSchool(b);
//                walletParentRepository.save(walletParent);
//            });
//        });
        return "------create wallet success-------";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/cashbook")
    public String createCashbook() {
//        cashbookCronjobs.createCronjobNowYear();
//        List<School> schoolList = schoolRepository.findAll();
//        int year = LocalDate.now().getYear();
//        LocalDate startDate = LocalDate.of(year, 1, 1);
//        LocalDate endDate = LocalDate.of(year, 12, 31);
//        schoolList.forEach(x -> {
//            Optional<FnCashBook> fnCashBookOptional = fnCashBookRepository.findBySchoolIdAndYear(idSchool, year);
//            if (fnCashBookOptional.isEmpty()) {
//                FnCashBook fnCashBook = new FnCashBook();
//                fnCashBook.setSchool(x);
//                fnCashBook.setYear(year);
//                fnCashBook.setStartDate(startDate);
//                fnCashBook.setEndDate(endDate);
//                fnCashBookRepository.save(fnCashBook);
//            }
//        });
        return "------create cashbook success-------";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/school-info")
    public String createSchoolInfo() {
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            Optional<SchoolInfo> schoolInfoOptional = schoolInfoRepository.findBySchoolId(idSchool);
//            if (schoolInfoOptional.isEmpty()) {
//                SchoolInfo model = new SchoolInfo();
//                model.setSchool(x);
//                schoolInfoRepository.save(model);
//            }
//        });
        return "------create school info success-------";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/config-attendance-employee")
    public String createConfigAttendanceEmployee() {
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            Optional<ConfigAttendanceEmployeeSchool> schoolInfoOptional = configAttendanceEmployeeSchoolRepository.findBySchoolId(idSchool);
//            if (schoolInfoOptional.isEmpty()) {
//                ConfigAttendanceEmployeeSchool model = new ConfigAttendanceEmployeeSchool();
//                model.setSchool(x);
//                configAttendanceEmployeeSchoolRepository.save(model);
//            }
//        });
        return "------create config-attendance-employee success-------";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/attendance-employee")
    public String createConfigEmployee() {
//        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findAll();
//        infoEmployeeSchoolList.forEach(x -> {
//            if (CollectionUtils.isEmpty(x.getConfigAttendanceEmployeeList())) {
//                ConfigAttendanceEmployeeSchool configAttendanceEmployeeSchool = configAttendanceEmployeeSchoolRepository.findBySchoolId(x.getSchool().getId()).orElseThrow();
//                ConfigAttendanceEmployee configAttendanceEmployee = modelMapper.map(configAttendanceEmployeeSchool, ConfigAttendanceEmployee.class);
//                configAttendanceEmployee.setInfoEmployeeSchool(x);
//                configAttendanceEmployee.setId(null);
//                configAttendanceEmployeeRepository.save(configAttendanceEmployee);
//            }
//        });
        return "------create attendance employee success-------";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/config-notify")
    public String createConfigNotify() {
        List<School> schoolList = schoolRepository.findAll();
        schoolList.forEach(x -> {
            if (x.getConfigNotifyPlus() == null) {
                ConfigNotifyPlus configNotifyPlus = new ConfigNotifyPlus();
                configNotifyPlus.setSchool(x);
                configNotifyPlusRepository.save(configNotifyPlus);
            }
            if (x.getConfigNotifyTeacher() == null) {
                ConfigNotifyTeacher configNotifyTeacher = new ConfigNotifyTeacher();
                configNotifyTeacher.setSchool(x);
                configNotifyTeacherRepository.save(configNotifyTeacher);
            }
            if (x.getConfigNotifyParent() == null) {
                ConfigNotifyParent configNotifyParent = new ConfigNotifyParent();
                configNotifyParent.setSchool(x);
                configNotifyParentRepository.save(configNotifyParent);
            }
        });
        return "------create notify success-------";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api")
    public String createApi(@RequestParam() String type) {
        Api api = new Api();
        api.setApiName("name");
        api.setApiUrl("url");
        api.setType(type);
        Api api1 = apiRepository.save(api);
        return "create api success " + api1.getId();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/web-system-title")
    public String createWebSystemTitle() {
        WebSystemTitle model = new WebSystemTitle();
        model.setTitle("this is title");
        model.setType(SystemConstant.TITLE_SYSTEM);
        model.setSource("source");
        WebSystemTitle webSystemTitleSaved = webSystemTitleRepository.save(model);
        return "create websystemtitle success: " + webSystemTitleSaved.getId();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/role")
    public String createRole(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataSystem(principal);
        List<School> schoolList = schoolRepository.findAll();
        schoolList.forEach(x -> {
            Optional<Role> roleConfig = roleRepository.findByIdSchoolAndDelActiveTrueAndRoleName(x.getId(), AppConstant.ROLE_CONFIG);
            if (roleConfig.isEmpty()) {
                Role role = new Role();
                role.setRoleName("Vai trò cấu hình");
                role.setType(AppTypeConstant.SCHOOL);
                role.setIdSchool(x.getId());
                role.setDescription("Có các quyền cấu hình nhà trường");
                roleRepository.save(role);
            }
            Optional<Role> roleCommon = roleRepository.findByIdSchoolAndDelActiveTrueAndRoleName(x.getId(), AppConstant.ROLE_COMMON);
            if (roleCommon.isEmpty()) {
                Role role = new Role();
                role.setRoleName("Vai trò chung");
                role.setType(AppTypeConstant.SCHOOL);
                role.setIdSchool(x.getId());
                role.setDescription("Có các quyền chung");
                roleRepository.save(role);
            }
            Optional<Role> roleFinance = roleRepository.findByIdSchoolAndDelActiveTrueAndRoleName(x.getId(), AppConstant.ROLE_FINANCE);
            if (roleFinance.isEmpty()) {
                Role role = new Role();
                role.setRoleName(AppConstant.ROLE_FINANCE);
                role.setType(AppTypeConstant.SCHOOL);
                role.setIdSchool(x.getId());
                role.setDescription("Có các quyền tài chính");
                roleRepository.save(role);
            }


            Optional<Role> roleTeacher = roleRepository.findByIdSchoolAndDelActiveTrueAndRoleName(x.getId(), AppConstant.ROLE_TEACHER);
            if (roleTeacher.isEmpty()) {
                Role role = new Role();
                role.setRoleName(AppConstant.ROLE_TEACHER);
                role.setType(AppTypeConstant.TEACHER);
                role.setIdSchool(x.getId());
                role.setDefaultStatus(AppConstant.APP_TRUE);
                role.setDescription("Các vai trò chung của giáo viên");
                roleRepository.save(role);
            }
        });
        return "------create role success-------";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/role/api")
    public String addApiForRole(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataSystem(principal);
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            Role roleConfig = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(x.getId(), "Vai trò cấu hình").orElseThrow();
//            if (CollectionUtils.isEmpty(roleConfig.getApiList())) {
//                List<Api> apiList = apiRepository.getApiFromTo(45, 63);
//                apiList.forEach(a -> roleRepository.insertRoleAPi(roleConfig.getId(), a.getId()));
//            }
//            Role roleCommon = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(x.getId(), "Vai trò chung").orElseThrow();
//            if (CollectionUtils.isEmpty(roleCommon.getApiList())) {
//                List<Api> apiList = apiRepository.getApiFromTo(64, 121);
//                apiList.forEach(a -> roleRepository.insertRoleAPi(roleCommon.getId(), a.getId()));
//            }
//        });

        List<School> schoolList = schoolRepository.findAll();
        schoolList.forEach(x -> {
            Role role = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(x.getId(), AppConstant.ROLE_COMMON).orElseThrow();
//            List<Long> idApiList = Arrays.asList(92L, 93L, 94L, 129L, 130L);
//            List<Api> apiList = apiRepository.findByIdInAndDelActiveTrue(idApiList);
            List<Api> apiList = apiRepository.findByIdGreaterThanEqualAndIdLessThanEqual(146, 149);
            apiList.forEach(a -> roleRepository.insertRoleAPi(role.getId(), a.getId()));
        });
        return "------create add api for role success-------";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/role/user")
    public String addRoleForPlus(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataSystem(principal);
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByAppTypeAndSchoolIdAndDelActiveTrue(AppTypeConstant.SCHOOL, x.getId());
//            infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(a -> a.getEmployee() != null).collect(Collectors.toList());
//            Role role = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(x.getId(), "Vai trò chung").orElseThrow();
//            infoEmployeeSchoolList.forEach(a -> {
//                MaUser maUser = a.getEmployee().getMaUser();
//                maUserRepository.insertUserRole(maUser.getId(), role.getId());
//            });
//        });

//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByAppTypeAndSchoolIdAndDelActiveTrue(AppTypeConstant.TEACHER, x.getId());
//            infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(a -> a.getEmployee() != null).collect(Collectors.toList());
//            List<Employee> employeeList = infoEmployeeSchoolList.stream().map(InfoEmployeeSchool::getEmployee).distinct().collect(Collectors.toList());
//            Role role = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(x.getId(), AppConstant.ROLE_TEACHER).orElseThrow();
//            employeeList.forEach(a -> {
//                MaUser maUser = a.getMaUser();
//                if (CollectionUtils.isEmpty(maUser.getRoleList())) {
//                    maUserRepository.insertUserRole(maUser.getId(), role.getId());
//                }
//            });
//        });
        return "------create add role for infoEmployee success-------";
    }


    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/dayOffClass/create")
    public String createDayOffClass() {
//        List<MaClass> maClassList = maClassRepository.findAll();
//        maClassList = maClassList.stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
//        List<LocalDate> dateList = new ArrayList<>();
//        LocalDate date = LocalDate.of(2021, 4, 4);
//        do {
//            dateList.add(date);
//            date = date.plusDays(7);
//        } while (date.getYear() <= 2024);
//        List<MaClass> finalMaClassList = maClassList;
//        System.out.println(dateList);
//        dateList.forEach(a -> {
//            finalMaClassList.forEach(x -> {
//                Optional<DayOffClass> dayOffClassOptional = dayOffClassRepository.findByMaClassIdAndDateAndDelActiveTrue(x.getId(), a);
//                if (dayOffClassOptional.isEmpty()) {
//                    DayOffClass dayOffClass = new DayOffClass();
//                    dayOffClass.setMaClass(x);
//                    dayOffClass.setDate(a);
//                    dayOffClass.setNote(AppConstant.DAY_OFF);
//                    dayOffClassRepository.save(dayOffClass);
//                }
//            });
//    });

        return "-------create dayOffClass ----------";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tabletest")
    public String createTableTest() {
        List<TableTest> tableTestList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            TableTest tableTest = new TableTest();
            tableTest.setFullName("Nguyễn Thành Bảo ");
            tableTest.setAge(17);
            tableTest.setAddress("Phường Cổ Nhuế, Bắc Từ Liêm, Hà Nội");
            tableTest.setPhone("0339834332");
            tableTest.setFullName(tableTest.getFullName().concat(String.valueOf(i)));
            tableTestList.add(tableTest);
            tableTestRepository.save(tableTest);
        }
//        tableTestRepository.saveAll(tableTestList);
        return "create tabletest success";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/group-out-kids")
    public String createGroupOutKids(@CurrentUser UserPrincipal principal) {
//        CommonValidate.checkDataSystem(principal);
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            GroupOutKids groupOutKids1 = new GroupOutKids();
//            groupOutKids1.setIdSchool(x.getId());
//            groupOutKids1.setName(AppConstant.GROUP_OUT_ABSENT);
//            groupOutKids1.setNote("Nhóm học sinh nghỉ học");
//            groupOutKids1.setDefaultStatus(AppConstant.APP_TRUE);
//
//            GroupOutKids groupOutKids2 = new GroupOutKids();
//            groupOutKids2.setIdSchool(x.getId());
//            groupOutKids2.setName(AppConstant.GROUP_OUT_LEAVE);
//            groupOutKids2.setNote("Nhóm học sinh ra trường");
//            groupOutKids2.setDefaultStatus(AppConstant.APP_TRUE);
//
//            GroupOutKids groupOutKids3 = new GroupOutKids();
//            groupOutKids3.setIdSchool(x.getId());
//            groupOutKids3.setName(AppConstant.GROUP_OUT_OTHER);
//            groupOutKids3.setDefaultStatus(AppConstant.APP_TRUE);
//
//            groupOutKidsRepository.save(groupOutKids1);
//            groupOutKidsRepository.save(groupOutKids2);
//            groupOutKidsRepository.save(groupOutKids3);
//        });
        return "create group out kids success";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/group-out-employee")
    public String createGroupOutEmployee(@CurrentUser UserPrincipal principal) {
//        CommonValidate.checkDataSystem(principal);
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            GroupOutEmployee groupOutEmployee1 = new GroupOutEmployee();
//            groupOutEmployee1.setIdSchool(x.getId());
//            groupOutEmployee1.setName(AppConstant.GROUP_OUT_ABSENT);
//            groupOutEmployee1.setNote("Nhóm nhân sự nghỉ làm");
//            groupOutEmployee1.setDefaultStatus(AppConstant.APP_TRUE);
//
//            GroupOutEmployee groupOutEmployee2 = new GroupOutEmployee();
//            groupOutEmployee2.setIdSchool(x.getId());
//            groupOutEmployee2.setName(AppConstant.GROUP_OUT_OTHER);
//            groupOutEmployee2.setDefaultStatus(AppConstant.APP_TRUE);
//
//            groupOutEmployeeRepository.save(groupOutEmployee1);
//            groupOutEmployeeRepository.save(groupOutEmployee2);
//        });
        return "create group out employee success";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/notification")
    public String createNotification(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataSystem(principal);
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            Long idSchool = x.getId();
//            List<NotificationManage> notificationManageList = notificationManageRepository.findByIdSchool(idSchool);
//            if (CollectionUtils.isEmpty(notificationManageList)) {
//                //notification.attendanceArrive
//                List<NotificationManageDate> dateAttendanceArrive = new ArrayList<>();
//                dateAttendanceArrive.add(this.setNotificationManageDate(0, 10, null, null, null));
//                List<NotificationManageDate> dateAttendanceArrive1 = new ArrayList<>();
//                dateAttendanceArrive1.add(this.setNotificationManageDate(0, 10, null, null, null));
//                //notification.attendanceLeave
//                List<NotificationManageDate> dateAttendanceLeave = new ArrayList<>();
//                dateAttendanceLeave.add(this.setNotificationManageDate(0, 19, null, null, null));
//                List<NotificationManageDate> dateAttendanceLeave1 = new ArrayList<>();
//                dateAttendanceLeave1.add(this.setNotificationManageDate(0, 19, null, null, null));
//                //notification.attendanceEat
//                List<NotificationManageDate> dateAttendanceEat = new ArrayList<>();
//                dateAttendanceEat.add(this.setNotificationManageDate(30, 10, null, null, null));
//                //notification.MEDICINE
//                List<NotificationManageDate> dateMedicine = new ArrayList<>();
//                dateMedicine.add(this.setNotificationManageDate(0, 10, null, null, null));
//                //notification.messageParent
//                List<NotificationManageDate> dateMessageParent = new ArrayList<>();
//                dateMessageParent.add(this.setNotificationManageDate(0, 9, null, null, null));
//                dateMessageParent.add(this.setNotificationManageDate(0, 12, null, null, null));
//                dateMessageParent.add(this.setNotificationManageDate(0, 15, null, null, null));
//                //notification.absent
//                List<NotificationManageDate> dateAbsent = new ArrayList<>();
//                dateAbsent.add(this.setNotificationManageDate(0, 9, null, null, null));
//                dateAbsent.add(this.setNotificationManageDate(0, 12, null, null, null));
//                dateAbsent.add(this.setNotificationManageDate(0, 15, null, null, null));
//                //notification.birthday
//                List<NotificationManageDate> dateBirthday = new ArrayList<>();
//                dateBirthday.add(this.setNotificationManageDate(30, 9, null, null, null));
//                //notification.fees
//                List<NotificationManageDate> dateFees = new ArrayList<>();
//                dateFees.add(this.setNotificationManageDate(0, 8, 5, null, null));
//                dateFees.add(this.setNotificationManageDate(0, 7, 10, null, null));
//                dateFees.add(this.setNotificationManageDate(0, 15, 6, null, null));
//                //thong bao mới
//                List<NotificationManageDate> dateHomeNew1 = new ArrayList<>();
//                dateHomeNew1.add(this.setNotificationManageDate(0, 14, null, null, null));
//                List<NotificationManageDate> dateHomeNew2 = new ArrayList<>();
//                dateHomeNew2.add(this.setNotificationManageDate(0, 14, null, null, null));
//                List<NotificationManageDate> dateHomeNew3 = new ArrayList<>();
//                dateHomeNew3.add(this.setNotificationManageDate(0, 14, null, null, null));
//                List<NotificationManageDate> dateHomeNew4 = new ArrayList<>();
//                dateHomeNew4.add(this.setNotificationManageDate(0, 14, null, null, null));
//                List<NotificationManageDate> dateHomeNew5 = new ArrayList<>();
//                dateHomeNew5.add(this.setNotificationManageDate(0, 14, null, null, null));
//                List<NotificationManageDate> dateHomeNew6 = new ArrayList<>();
//                dateHomeNew6.add(this.setNotificationManageDate(0, 14, null, null, null));
//                //Th4ng báo học phí
//                List<NotificationManageDate> thongBaoHocPhi = new ArrayList<>();
//                thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 10, null, 30));
//                thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 15, null, 50));
//                thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 20, null, 70));
//                thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 25, null, 85));
//                thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 28, null, 100));
//                //Th4ng báo c4ng luong
//                List<NotificationManageDate> thongBaoCongLuong = new ArrayList<>();
//                thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 10, null, 30));
//                thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 15, null, 50));
//                thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 20, null, 70));
//                thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 25, null, 85));
//                thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 28, null, 100));
//                //Thu chi
//                List<NotificationManageDate> thongBaoThuChi = new ArrayList<>();
//                thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 10, null, null));
//                thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 15, null, null));
//                thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 20, null, null));
//                thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 25, null, null));
//                thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 28, null, null));
//                //===========================
//                //Điểm danh đến
//                this.createNotificationManage("Thông báo chưa điểm danh đến", "Có {number} học sinh trong lớp {class_name} chưa được điểm danh đến.", NotificationConstant.NOTIFICATION_ATTENDANCE_ARRIVE, AppTypeConstant.TEACHER, 0, idSchool, dateAttendanceArrive1);
//                this.createNotificationManage("Thông báo chưa điểm danh đến", "Học sinh {kid_name} chưa được điểm danh đến.", NotificationConstant.NOTIFICATION_ATTENDANCE_ARRIVE, AppTypeConstant.PARENT, 0, idSchool, dateAttendanceArrive);
//                //Điểm danh về
//                this.createNotificationManage("Thông báo chưa điểm danh về", "Có {number} học sinh trong lớp {class_name} chưa được điểm danh về.", NotificationConstant.NOTIFICATION_ATTENDANCE_LEAVE, AppTypeConstant.TEACHER, 0, idSchool, dateAttendanceLeave1);
//                this.createNotificationManage("Thông báo chưa điểm danh về", "Học sinh {kid_name} chưa được điểm danh về.", NotificationConstant.NOTIFICATION_ATTENDANCE_LEAVE, AppTypeConstant.PARENT, 0, idSchool, dateAttendanceLeave);
//                //Điểm danh ăn
//                this.createNotificationManage("Thông báo chưa điểm danh ăn", "Có {number} học sinh trong lớp {class_name} chưa được điểm danh ăn.", NotificationConstant.NOTIFICATION_ATTENDANCE_EAT, AppTypeConstant.TEACHER, 0, idSchool, dateAttendanceEat);
//                //Dặn thuốc teacher
//                this.createNotificationManage("Thông báo dặn thuốc chưa xác nhận", "Có {number} đơn thuốc trong lớp {class_name} chưa xác nhận.", NotificationConstant.NOTIFICATION_MEDICINE, AppTypeConstant.TEACHER, 0, idSchool, dateMedicine);
//                //Lời nhắn teacher
//                this.createNotificationManage("Thông báo lời nhắn chưa xác nhận", "Có {number} lời nhắn trong lớp {class_name} chưa xác nhận.", NotificationConstant.NOTIFICATION_MESSAGE, AppTypeConstant.TEACHER, 0, idSchool, dateMessageParent);
//                //Xin nghỉ teacher
//                this.createNotificationManage("Thông báo xin nghỉ chưa xác nhận", "Có {number} xin nghỉ trong lớp {class_name} chưa xác nhận.", NotificationConstant.NOTIFICATION_ABSENT, AppTypeConstant.TEACHER, 0, idSchool, dateAbsent);
//                //Sinh nhật
//                this.createNotificationManage("Thông báo sinh nhật học sinh", "Có {number} học sinh trong lớp {class_name} sinh nhật hôm nay.", NotificationConstant.NOTIFICATION_BIRTHDAY, AppTypeConstant.TEACHER, 0, idSchool, dateBirthday);
//                //Hóa đơn học phí parent
//                this.createNotificationManage("Thông báo chưa hoàn thành học phí", "Học sinh {kid_name} chưa hoàn thành học phí tháng {month}.", NotificationConstant.NOTIFICATION_FEES, AppTypeConstant.PARENT, 0, idSchool, dateFees);
//                //Tổng hợp home parent
//                this.createNotificationManage("", "- {number} lời nhắn mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew1);
//                this.createNotificationManage("", "- {number} đơn dặn thuốc mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew2);
//                this.createNotificationManage("", "- {number} đơn xin nghỉ mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew3);
//                this.createNotificationManage("", "- {number} album ảnh mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew4);
//                this.createNotificationManage("", "- {number} nhật xét mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew5);
//                this.createNotificationManage("", "- {number} góp ý mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew6);
//                //Thông báo học phí plus
//                this.createNotificationManage("Thông báo học phí tháng {month}", "Chi tiết học phí đến ngày {date}\n" +
//                        "- Tổng tiền học phí: {moneyTotal} VNĐ\n" +
//                        "- Tổng tiền đã thu:  {moneyPaid} VNĐ\n" +
//                        "- Tổng tiền chưa thu:  {moneyNoPaid} VNĐ\n" +
//                        "- Số học sinh đã hoàn thành hóa đơn: {numberSuccess}\n" +
//                        "- Số học sinh chưa hoàn thành hóa đơn: {numberNoSuccess}", NotificationConstant.NOTIFICATION_FEES, AppTypeConstant.SCHOOL, 0, idSchool, thongBaoHocPhi);//Thông báo học phí
//                //Thông báo công lương plus
//                this.createNotificationManage("Thông báo công lương tháng {month}", "Chi tiết công lương đến ngày {date}\n" +
//                        "- Tổng tiền công lương cần chi: {moneyTotal} VNĐ\n" +
//                        "- Tổng tiền đã chi: {moneyPaid} VNĐ\n" +
//                        "- Tổng tiền chưa chi: {moneyNoPaid} VNĐ\n" +
//                        "- Số nhân sự đã hoàn thành hóa đơn: {numberSuccess}\n" +
//                        "- Số nhân sự chưa hoàn thành hóa đơn: {numberNoSuccess}", NotificationConstant.NOTIFICATION_SALARY, AppTypeConstant.SCHOOL, 0, idSchool, thongBaoCongLuong);
//                //Thông báo thu chi nội bộ plus
//                this.createNotificationManage("Thông báo thu chi nội bộ tháng {month}", "Tổng tiền thu chi nội bộ tính đến ngày {date}\n" +
//                        "- Tổng tiền phiếu thu: {moneyIn} VNĐ\n" +
//                        "- Tổng tiền phiếu chi: {moneyOut} VNĐ", NotificationConstant.NOTIFICATION_CASH_INTERNAL, AppTypeConstant.SCHOOL, 0, idSchool, thongBaoThuChi);
//            }
//        });
        return "create notification success";
    }

    private void createNotificationManage(String title, String content, String type, String typeReceive, int month, Long idSchool, List<NotificationManageDate> dateList) {
        NotificationManage notificationManage = new NotificationManage();
        notificationManage.setTitle(title);
        notificationManage.setContent(content);
        notificationManage.setType(type);
        notificationManage.setTypeReceive(typeReceive);
        notificationManage.setMonth(month);
        notificationManage.setIdSchool(idSchool);
        if (AppTypeConstant.PARENT.equals(typeReceive)) {
            notificationManage.setSortNumber(1);
        } else if (AppTypeConstant.TEACHER.equals(typeReceive)) {
            notificationManage.setSortNumber(2);
        } else if (AppTypeConstant.SCHOOL.equals(typeReceive)) {
            notificationManage.setSortNumber(3);
        }
//        notificationManageRepository.save(notificationManage);
//        dateList.forEach(x->{
//            x.setNotificationManage(notificationManage);
//            notificationManageDateRepository.save(x);
//        });
        dateList.forEach(x -> x.setNotificationManage(notificationManage));
        notificationManage.setNotificationManageDateList(dateList);
        notificationManageRepository.save(notificationManage);
    }

    private NotificationManageDate setNotificationManageDate(Integer minute, Integer hour, Integer day, Integer month, Integer percent) {
        NotificationManageDate x = new NotificationManageDate();
        x.setMinute(minute);
        x.setHour(hour);
        x.setDay(day);
        x.setMonth(month);
        x.setPercent(percent);
        return x;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-verifycode")
    public String createVerifyCode(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataSystem(principal);
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            SchoolConfig schoolConfig = x.getSchoolConfig();
//            schoolConfig.setVerifyCode(GenerateCode.getLetterUpperCase());
//            schoolConfigRepository.save(schoolConfig);
//        });
        return "create verifyCode success";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test-1")
    public String test1(@CurrentUser UserPrincipal principal) {
        System.out.println("my teacher 123");
        return "no verify 123333";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test-2")
    public String test2(@CurrentUser UserPrincipal principal) {
        return "test 2222 3333";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/onecam")
    public String createNewCame(@CurrentUser UserPrincipal principal) {
//        CommonValidate.checkDataSystem(principal);
//        List<School> schoolList = schoolRepository.findAll();
//        schoolList.forEach(x -> {
//            cycleMoneyService.createCycleMoneyDefault(x);
//        });
        return "cycle config";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-package-root")
    public String createPackageRoot(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataSystem(principal);
        List<School> schoolList = schoolRepository.findAll();
        schoolList.forEach(x -> {
            fnPackageService.createPackageRoot(x.getId());
        });
        return "create package root success";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create-dynamic")
    public String createPackageNotifyDynamic(@CurrentUser UserPrincipal principal) {
        CommonValidate.checkDataSystem(principal);
        List<School> schoolList = schoolRepository.findAll();
        schoolList.forEach(x -> {
//            schoolServiceImpl.abc(x.getId());
        });
        return "create package root success";
    }


}
