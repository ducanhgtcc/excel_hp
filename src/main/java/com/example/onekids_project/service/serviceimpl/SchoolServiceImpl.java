package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.dto.SchoolDTO;
import com.example.onekids_project.entity.agent.Agent;
import com.example.onekids_project.entity.employee.AccountType;
import com.example.onekids_project.entity.employee.GroupOutEmployee;
import com.example.onekids_project.entity.finance.CashInternal.FnPeopleType;
import com.example.onekids_project.entity.kids.GroupOutKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.*;
import com.example.onekids_project.entity.usermaster.MaAdmin;
import com.example.onekids_project.enums.StudentStatusEnum;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.SchoolSmsRequest;
import com.example.onekids_project.master.request.school.GroupTypeRequest;
import com.example.onekids_project.master.response.SmsSchoolResponse;
import com.example.onekids_project.master.response.school.ConfigNotifyResponse;
import com.example.onekids_project.master.response.school.icon.*;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.school.CreateSchoolRequest;
import com.example.onekids_project.request.school.SearchSchoolRequest;
import com.example.onekids_project.request.school.UpdateForSchoolRequest;
import com.example.onekids_project.request.school.UpdateSchoolRequest;
import com.example.onekids_project.request.system.SchoolConfigSeachRequest;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.school.ListSchoolResponse;
import com.example.onekids_project.response.school.SchoolOtherResponse;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.response.school.SchoolUniqueResponse;
import com.example.onekids_project.response.schoolconfig.SchoolDataResponse;
import com.example.onekids_project.response.system.SchoolSystemConfigIconResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.service.servicecustom.cashbook.FnCashBookService;
import com.example.onekids_project.service.servicecustom.cashinternal.PeopleTypeService;
import com.example.onekids_project.service.servicecustom.config.SchoolConfigService;
import com.example.onekids_project.service.servicecustom.finance.CycleMoneyService;
import com.example.onekids_project.service.servicecustom.finance.FnPackageService;
import com.example.onekids_project.service.servicecustom.onecam.OneCamConfigService;
import com.example.onekids_project.service.servicecustom.onecam.OneCamNewsService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private SchoolSmsRepository schoolSmsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SchoolConfigService schoolConfigService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private AppIconParentService appIconParentService;

    @Autowired
    private AppIconTeacherService appIconTeacherService;

    @Autowired
    private AppIconPlusService appIconPlusService;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private BirthdaySampleService birthdaySampleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AttendanceConfigService attendanceConfigService;
    @Autowired
    private OneCamNewsService oneCamNewsService;
    @Autowired
    private FnPackageService fnPackageService;

    @Autowired
    private ConfigPlusService configPlusService;

    @Autowired
    private PeopleTypeRepository peopleTypeRepository;

    @Autowired
    private PeopleTypeService peopleTypeService;

    @Autowired
    private ConfigAttendanceEmployeeSchoolRepository configAttendanceEmployeeSchoolRepository;

    @Autowired
    private FnCashBookService fnCashBookService;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;
    @Autowired
    private ConfigNotifyPlusRepository configNotifyPlusRepository;
    @Autowired
    private ConfigNotifyTeacherRepository configNotifyTeacherRepository;
    @Autowired
    private ConfigNotifyParentRepository configNotifyParentRepository;
    @Autowired
    private GroupOutKidsRepository groupOutKidsRepository;
    @Autowired
    private GroupOutEmployeeRepository groupOutEmployeeRepository;
    @Autowired
    private NotificationManageRepository notificationManageRepository;
    @Autowired
    private NotificationManageDateRepository notificationManageDateRepository;
    @Autowired
    private AppIconTeacherRepository appIconTeacherRepository;
    @Autowired
    private AppIconPlusRepository appIconPlusRepository;
    @Autowired
    private AppIconParentRepository appIconParentRepository;
    @Autowired
    private OneCamConfigService oneCamConfigService;
    @Autowired
    private CycleMoneyService cycleMoneyService;


    @Override
    public List<SchoolOtherResponse> findSchoolInAgent(Long idAgent) {
        List<School> schoolList = schoolRepository.findByAgentIdAndDelActiveTrueOrderBySchoolName(idAgent);
        List<SchoolOtherResponse> responseList = listMapper.mapList(schoolList, SchoolOtherResponse.class);
        return responseList;
    }

    @Override
    public List<SchoolOtherResponse> getSchoolBriefService() {
        return listMapper.mapList(schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue(), SchoolOtherResponse.class);
    }

    @Override
    public List<SchoolUniqueResponse> findSchoolUnique() {
        List<School> schoolList = schoolRepository.findAllByDelActiveTrue();
        if (CollectionUtils.isEmpty(schoolList)) {
            return null;
        }
        List<SchoolUniqueResponse> schoolUniqueResponseList = listMapper.mapList(schoolList, SchoolUniqueResponse.class);
        return schoolUniqueResponseList;
    }

    @Override
    public ListSchoolResponse findAllSchool(Pageable pageable) {
        List<School> schoolList = schoolRepository.findAllSchool(pageable);
        if (CollectionUtils.isEmpty(schoolList)) {
            return null;
        }
        List<SchoolDTO> schoolDTOList = listMapper.mapList(schoolList, SchoolDTO.class);
        ListSchoolResponse listSchoolResponse = new ListSchoolResponse();
        listSchoolResponse.setSchoolList(schoolDTOList);

        return listSchoolResponse;
    }

    @Override
    public Optional<SchoolResponse> findByIdSchool(Long id) {
        School school = schoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        return Optional.ofNullable(modelMapper.map(school, SchoolResponse.class));
    }

    @Override
    public ListSchoolResponse searchSchool(SearchSchoolRequest request) {
        ListSchoolResponse response = new ListSchoolResponse();
        List<School> schoolList = schoolRepository.searchSchool(request);
        long count = schoolRepository.countsearchSchool(request);
        List<SchoolDTO> schoolDTOList = listMapper.mapList(schoolList, SchoolDTO.class);
        schoolDTOList.forEach(x -> {
            long countStudy = kidsRepository.countByIdSchoolAndDelActiveTrueAndKidStatus(x.getId(), KidsStatusConstant.STUDYING);
            x.setCountStudy(countStudy);
        });
        response.setSchoolList(schoolDTOList);
        response.setTotal(count);
        return response;
    }

    @Override
    public List<ExcelResponse> searchSchoolExport(List<Long> idList) {
        List<ExcelResponse> responseList = new ArrayList<>();
        List<School> schoolList = schoolRepository.findByIdInAndDelActiveTrue(idList);
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> headerList = new ArrayList<>();
        List<ExcelData> bodyList = new ArrayList<>();
        headerList.addAll(ExportExcelUtils.setHeaderExcel(List.of("Thông tin danh sách các trường")));
        int i = 1;
        for (School x : schoolList) {
            Long idSchool = x.getId();
            int studyingNumber = kidsRepository.countByIdSchoolAndKidStatusAndDelActiveTrue(idSchool, KidsStatusConstant.STUDYING);
            int waitNumber = kidsRepository.countByIdSchoolAndKidStatusAndDelActiveTrue(idSchool, KidsStatusConstant.STUDY_WAIT);
            int reserveNumber = kidsRepository.countByIdSchoolAndKidStatusAndDelActiveTrue(idSchool, KidsStatusConstant.RESERVE_NAME);
            int leaveNumber = kidsRepository.countByIdSchoolAndKidStatusAndDelActiveTrue(idSchool, KidsStatusConstant.LEAVE_SCHOOL);
            List<String> bodyStringList = Arrays.asList(String.valueOf(i++), x.getSchoolName(), x.getAgent().getAgentName(), x.isSchoolActive() ? "Đã kích hoạt" : "Chưa kích hoạt", String.valueOf(studyingNumber), String.valueOf(waitNumber), String.valueOf(reserveNumber), String.valueOf(leaveNumber),
                    String.valueOf(x.getSmsBudget()), String.valueOf(x.getSmsTotal()), String.valueOf(x.getSmsUsed()), String.valueOf(x.getSmsTotal() - x.getSmsUsed()), x.getSchoolPhone(), x.getSchoolEmail(), x.getSchoolAddress());
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
        }
        response.setSheetName("Danh sách trường");
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Transactional
    @Override
    public boolean createSchool(CreateSchoolRequest createSchoolRequest) throws IOException {
        School newSchool = modelMapper.map(createSchoolRequest, School.class);
        Agent agent = agentRepository.findByIdAndDelActive(createSchoolRequest.getIdAgent(), AppConstant.APP_TRUE).orElseThrow();
        newSchool.setAgent(agent);
        newSchool.setSchoolCode(GenerateCode.codeSchool());
        Optional<School> schoolOptional = schoolRepository.findBySchoolCode(newSchool.getSchoolCode());
        while (schoolOptional.isPresent()) {
            newSchool.setSchoolCode(DataUtils.randomSchoolCode());
            schoolOptional = schoolRepository.findBySchoolCode(newSchool.getSchoolCode());
        }
        if (createSchoolRequest.getMultipartFileAvatar() != null) {
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(createSchoolRequest.getMultipartFileAvatar(), SystemConstant.ID_SYSTEM, UploadDownloadConstant.AVATAR);
            newSchool.setSchoolAvatar(handleFileResponse.getUrlWeb());
            newSchool.setSchoolLocalAvatar(handleFileResponse.getUrlLocal());
        }
        School savedSchool = schoolRepository.save(newSchool);

        //tạo các thông tin khác liên quan đến school
        this.createSchoolBySchool(savedSchool);
        this.createExtraSchool(savedSchool.getId());
        return true;
    }


    @Override
    @Transactional
    public SchoolResponse updateSchool(UpdateSchoolRequest updateSchoolRequest) throws IOException {
        School odlSchool = schoolRepository.findByIdAndDelActiveTrue(updateSchoolRequest.getId()).orElseThrow();
        modelMapper.map(updateSchoolRequest, odlSchool);
        if (updateSchoolRequest.getMultipartFileAvatar() != null) {
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(updateSchoolRequest.getMultipartFileAvatar(), SystemConstant.ID_SYSTEM, UploadDownloadConstant.AVATAR);
            odlSchool.setSchoolAvatar(handleFileResponse.getUrlWeb());
            odlSchool.setSchoolLocalAvatar(handleFileResponse.getUrlLocal());
        }


        School newSchool = schoolRepository.save(odlSchool);
        SchoolResponse schoolResponse = modelMapper.map(newSchool, SchoolResponse.class);
        return schoolResponse;
    }


    @Override
    public boolean deleteSchool(Long id) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE);
        List<MaAdmin> maAdmins = schoolOptional.get().getMaAdminList();
        List<Kids> kidsList = kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatus(id, StudentStatusEnum.STUDYING.toString());
        if (kidsList.size() > 0 && maAdmins.size() > 0) {
            return false;
        } else {
            School deleteSchool = schoolOptional.get();
            deleteSchool.setDelActive(false);
            schoolRepository.save(deleteSchool);
            return true;
        }
    }

    @Override
    @Transactional
    public int saveSchoolSms(SchoolSmsRequest schoolSmsRequest) {
        int checkCount = 0;
        for (Long idSchool : schoolSmsRequest.getIdSchoolList()) {
            School school1 = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow(() -> new NotFoundException("Not Found School By Id"));
            long conlai = school1.getAgent().getSmsTotal() - school1.getAgent().getSmsUsed();
            if (conlai < schoolSmsRequest.getSmsAdd()) {
                checkCount++;
                continue;
            }
            Agent agent = school1.getAgent();
            agent.setSmsUsed(agent.getSmsUsed() + schoolSmsRequest.getSmsAdd());
            agentRepository.save(agent);
            SchoolSms schoolSms = new SchoolSms();
            schoolSms.setContent(schoolSmsRequest.getContent());
            schoolSms.setSms_add(schoolSmsRequest.getSmsAdd());
            schoolSms.setSmsDate(LocalDateTime.now());
            School school = schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE).get();
            school.setSmsTotal(school.getSmsTotal() + schoolSms.getSms_add());
            schoolSms.setSchool(school);
            schoolSmsRepository.save(schoolSms);
        }
        return checkCount;
    }

    @Override
    public List<SmsSchoolResponse> findSchoolSmsByIdSchool(Long idSchool) {
        List<SchoolSms> schoolSmsList = schoolSmsRepository.findBySchoolId(idSchool, Sort.by("id").descending());
        List<SmsSchoolResponse> smsSchoolResponseList = new ArrayList<>();
        schoolSmsList.stream().forEach(schoolSms -> {
            SmsSchoolResponse smsSchoolResponse = new SmsSchoolResponse();
            smsSchoolResponse.setContent(schoolSms.getContent());
            smsSchoolResponse.setSmsDate(schoolSms.getSmsDate().toString());
            smsSchoolResponse.setNumberSms(schoolSms.getSms_add());
            smsSchoolResponse.setCreateBy(maUserRepository.findByIdAndDelActiveTrue(schoolSms.getIdCreated()).get().getFullName());
            smsSchoolResponseList.add(smsSchoolResponse);
        });
        return smsSchoolResponseList;
    }

    @Override
    public List<SchoolSystemConfigIconResponse> findAllSchoolConfigIcon(SchoolConfigSeachRequest
                                                                                schoolConfigSeachRequest) {
        List<School> schoolList = schoolRepository.searchSchoolForConfigIcon(schoolConfigSeachRequest);
        if (CollectionUtils.isEmpty(schoolList)) {
            return null;
        }
        List<SchoolSystemConfigIconResponse> schoolSystemConfigIconResponseList = listMapper.mapList(schoolList, SchoolSystemConfigIconResponse.class);
        return schoolSystemConfigIconResponseList;
    }

    @Override
    public boolean updateMultiActiveSchool(List<Long> ids, Boolean activeOrUnActive) {
        if (ids != null) {
            for (Long id : ids) {
                School school = schoolRepository.findById(id).get();
                school.setSchoolActive(activeOrUnActive);
                schoolRepository.save(school);
            }
        }
        return true;
    }

    @Override
    public ConfigNotifyResponse getConfigNotify(UserPrincipal principal, Long idSchool) {
        CommonValidate.checkDataAdmin(principal);
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        return modelMapper.map(school, ConfigNotifyResponse.class);
    }

    @Override
    public boolean updateConfigNotify(UserPrincipal principal, ConfigNotifyResponse configNotifyResponse) {
        CommonValidate.checkDataAdmin(principal);
        School school = schoolRepository.findByIdAndDelActiveTrue(configNotifyResponse.getId()).orElseThrow();
        modelMapper.map(configNotifyResponse, school);
        schoolRepository.save(school);
        return true;
    }

    @Override
    public IconLockResponse getIconLockConfig(UserPrincipal principal, Long idSchool) {
        CommonValidate.checkDataAdmin(principal);
        IconLockResponse response = new IconLockResponse();
        AppIconPlus appIconPlus = appIconPlusRepository.findBySchoolId(idSchool).orElseThrow();
        AppIconTeacher appIconTeacher = appIconTeacherRepository.findBySchoolId(idSchool).orElseThrow();
        AppIconParent appIconParent = appIconParentRepository.findBySchoolId(idSchool).orElseThrow();
        IconLockPlusExtend iconLockPlus = modelMapper.map(appIconPlus, IconLockPlusExtend.class);
        IconLockTeacherExtend iconLockTeacher = modelMapper.map(appIconTeacher, IconLockTeacherExtend.class);
        IconLockParentExtend iconLockParent = modelMapper.map(appIconParent, IconLockParentExtend.class);
        response.setIconLockPlus(iconLockPlus);
        response.setIconLockTeacher(iconLockTeacher);
        response.setIconLockParent(iconLockParent);
        return response;
    }

    @Override
    public boolean updateIconLockConfig(UserPrincipal principal, IconLockRequest request) {
        AppIconPlus appIconPlus = appIconPlusRepository.findById(request.getIconLockPlus().getId()).orElseThrow();
        AppIconTeacher appIconTeacher = appIconTeacherRepository.findById(request.getIconLockTeacher().getId()).orElseThrow();
        AppIconParent appIconParent = appIconParentRepository.findById(request.getIconLockParent().getId()).orElseThrow();
        modelMapper.map(request.getIconLockPlus(), appIconPlus);
        modelMapper.map(request.getIconLockTeacher(), appIconTeacher);
        modelMapper.map(request.getIconLockParent(), appIconParent);
        appIconPlusRepository.save(appIconPlus);
        appIconTeacherRepository.save(appIconTeacher);
        appIconParentRepository.save(appIconParent);
        return true;
    }

    @Override
    public SchoolDataResponse getSchoolData(UserPrincipal principal) {
        School school = schoolRepository.findByIdAndDelActiveTrue(principal.getIdSchoolLogin()).orElseThrow();
        SchoolDataResponse response = new SchoolDataResponse();
        modelMapper.map(school, response);
        response.setVerifyCode(school.getSchoolConfig().getVerifyCode());
        return response;
    }

    @Override
    public boolean updateSchoolData(UserPrincipal principal, UpdateForSchoolRequest request) throws IOException {
        //mở ra
//        CommonValidate.checkDataPlus(principal);
        School school = schoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        if (!school.getId().equals(principal.getIdSchoolLogin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể cập nhật thông tin trường");
        }
        if (request.getMultipartFile() != null) {
            String urlLocalOld = school.getSchoolAvatar();
            HandleFileUtils.deletePictureInFolder(urlLocalOld);
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(request.getMultipartFile(), SystemConstant.ID_SYSTEM, UploadDownloadConstant.AVATAR);
            school.setSchoolAvatar(handleFileResponse.getUrlWeb());
            school.setSchoolLocalAvatar(handleFileResponse.getUrlLocal());
        }
        modelMapper.map(request, school);
        schoolRepository.save(school);
        return true;
    }

    @Override
    public void updateGroupTypeService(GroupTypeRequest request) {
        List<School> schoolList = schoolRepository.findByIdInAndDelActiveTrue(request.getIdList());
        String groupType = request.getGroupType();
        schoolList.forEach(x -> {
            x.setGroupType(groupType);
        });
        schoolRepository.saveAll(schoolList);
    }

    /**
     * tạo các thông tin liên quan của một trường
     *
     * @param idSchool
     */
    private void createExtraSchool(Long idSchool) {
        CommonUtil.createFolderSchool(idSchool);
        SchoolConfig schoolConfig = schoolConfigService.createSchoolConfig(idSchool);
        sysConfigService.createSysConfigForSchool(idSchool);
        appIconParentService.createAppIconParent(idSchool);
        appIconTeacherService.createAppIconTeacher(idSchool);
        appIconPlusService.createAppIconPlus(idSchool);
        this.saveAccountType(idSchool);
        attendanceConfigService.createFirstAttendanceDateConfig(idSchool, schoolConfig);
        this.createGroupOutKids(idSchool);
        this.createGroupOutEmployee(idSchool);
        this.setNotificationManage(idSchool);
        oneCamNewsService.createOneCamNews(idSchool);
        fnPackageService.createPackageRoot(idSchool);
    }

    private void createGroupOutKids(Long idSchool) {
        GroupOutKids groupOutKids1 = new GroupOutKids();
        groupOutKids1.setIdSchool(idSchool);
        groupOutKids1.setName(AppConstant.GROUP_OUT_ABSENT);
        groupOutKids1.setNote("Nhóm học sinh nghỉ học");
        groupOutKids1.setDefaultStatus(AppConstant.APP_TRUE);

        GroupOutKids groupOutKids2 = new GroupOutKids();
        groupOutKids2.setIdSchool(idSchool);
        groupOutKids2.setName(AppConstant.GROUP_OUT_LEAVE);
        groupOutKids2.setNote("Nhóm học sinh ra trường");
        groupOutKids2.setDefaultStatus(AppConstant.APP_TRUE);

        GroupOutKids groupOutKids3 = new GroupOutKids();
        groupOutKids3.setIdSchool(idSchool);
        groupOutKids3.setName(AppConstant.GROUP_OUT_OTHER);
        groupOutKids3.setDefaultStatus(AppConstant.APP_TRUE);

        groupOutKidsRepository.save(groupOutKids1);
        groupOutKidsRepository.save(groupOutKids2);
        groupOutKidsRepository.save(groupOutKids3);
    }

    private void createGroupOutEmployee(Long idSchool) {
        GroupOutEmployee groupOutEmployee1 = new GroupOutEmployee();
        groupOutEmployee1.setIdSchool(idSchool);
        groupOutEmployee1.setName(AppConstant.GROUP_OUT_ABSENT);
        groupOutEmployee1.setNote("Nhóm nhân sự nghỉ làm");
        groupOutEmployee1.setDefaultStatus(AppConstant.APP_TRUE);

        GroupOutEmployee groupOutEmployee2 = new GroupOutEmployee();
        groupOutEmployee2.setIdSchool(idSchool);
        groupOutEmployee2.setName(AppConstant.GROUP_OUT_OTHER);
        groupOutEmployee2.setDefaultStatus(AppConstant.APP_TRUE);

        groupOutEmployeeRepository.save(groupOutEmployee1);
        groupOutEmployeeRepository.save(groupOutEmployee2);
    }

    private void createSchoolBySchool(School school) {
        birthdaySampleService.createBirthSampleForSchool(school);
        configPlusService.createConfigPlus(school);
        peopleTypeService.createPeopleTypeSchool(school);
        fnCashBookService.createCashBook(school);
        roleService.createRoleForSchool(school);
        roleService.addApiForRoleDefault(school);
        oneCamConfigService.createOneCamConfigDefault(school);
        cycleMoneyService.createCycleMoneyDefault(school);
        this.createConfigAttendanceEmployee(school);
        this.createPeopleType(school);
        this.createSchoolInfo(school);
        this.createConfigNotify(school);
    }


    public void saveAccountType(Long idSchool) {
        AccountType accountType1 = new AccountType();
        accountType1.setIdSchool(idSchool);
        accountType1.setName(AccountTypeConstant.MANAGER);

        AccountType accountType2 = new AccountType();
        accountType2.setIdSchool(idSchool);
        accountType2.setName(AccountTypeConstant.EMPLOYEE);

        AccountType accountType3 = new AccountType();
        accountType3.setIdSchool(idSchool);
        accountType3.setName(AccountTypeConstant.TEACHER);

        AccountType accountType4 = new AccountType();
        accountType4.setIdSchool(idSchool);
        accountType4.setName(AccountTypeConstant.COLLABORATOR);

        AccountType accountType5 = new AccountType();
        accountType5.setIdSchool(idSchool);
        accountType5.setName(AccountTypeConstant.OTHER);

        accountTypeRepository.save(accountType1);
        accountTypeRepository.save(accountType2);
        accountTypeRepository.save(accountType3);
        accountTypeRepository.save(accountType4);
        accountTypeRepository.save(accountType5);
    }

    private void createConfigNotify(School x) {
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
    }

    private void createPeopleType(School school) {
        Optional<FnPeopleType> fnPeopleTypeOptional = peopleTypeRepository.findByIdSchoolAndDefaultStatusTrue(school.getId());
        if (fnPeopleTypeOptional.isEmpty()) {
            FnPeopleType fnPeopleType = new FnPeopleType();
            fnPeopleType.setIdSchool(school.getId());
            fnPeopleType.setName(AppConstant.SCHOOL_NAME);
            fnPeopleType.setDefaultStatus(AppConstant.APP_TRUE);
            fnPeopleType.setType(FinanceConstant.INTERNAL);
            fnPeopleType.setAddress(school.getSchoolAddress());
            fnPeopleType.setEmail(school.getSchoolEmail());
            fnPeopleType.setPhone(school.getSchoolPhone());
            peopleTypeRepository.save(fnPeopleType);
        }
    }

    private void createConfigAttendanceEmployee(School school) {
        ConfigAttendanceEmployeeSchool model = new ConfigAttendanceEmployeeSchool();
        model.setSchool(school);
        configAttendanceEmployeeSchoolRepository.save(model);
    }

    private void createSchoolInfo(School school) {
        SchoolInfo model = new SchoolInfo();
        model.setSchool(school);
        schoolInfoRepository.save(model);
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

    public void abc(Long idSchool) {
        this.setNotificationManage(idSchool);
    }

    private void setNotificationManage(Long idSchool) {
        //notification.attendanceArrive
        List<NotificationManageDate> dateAttendanceArrive = new ArrayList<>();
        dateAttendanceArrive.add(this.setNotificationManageDate(0, 10, null, null, null));
        List<NotificationManageDate> dateAttendanceArrive1 = new ArrayList<>();
        dateAttendanceArrive1.add(this.setNotificationManageDate(0, 10, null, null, null));
        //notification.attendanceLeave
        List<NotificationManageDate> dateAttendanceLeave = new ArrayList<>();
        dateAttendanceLeave.add(this.setNotificationManageDate(0, 19, null, null, null));
        List<NotificationManageDate> dateAttendanceLeave1 = new ArrayList<>();
        dateAttendanceLeave1.add(this.setNotificationManageDate(0, 19, null, null, null));
        //notification.attendanceEat
        List<NotificationManageDate> dateAttendanceEat = new ArrayList<>();
        dateAttendanceEat.add(this.setNotificationManageDate(30, 10, null, null, null));
        //notification.MEDICINE
        List<NotificationManageDate> dateMedicine = new ArrayList<>();
        dateMedicine.add(this.setNotificationManageDate(0, 10, null, null, null));
        //notification.messageParent
        List<NotificationManageDate> dateMessageParent = new ArrayList<>();
        dateMessageParent.add(this.setNotificationManageDate(0, 9, null, null, null));
        dateMessageParent.add(this.setNotificationManageDate(0, 12, null, null, null));
        dateMessageParent.add(this.setNotificationManageDate(0, 15, null, null, null));
        //notification.absent
        List<NotificationManageDate> dateAbsent = new ArrayList<>();
        dateAbsent.add(this.setNotificationManageDate(0, 9, null, null, null));
        dateAbsent.add(this.setNotificationManageDate(0, 12, null, null, null));
        dateAbsent.add(this.setNotificationManageDate(0, 15, null, null, null));
        //notification.birthday
        List<NotificationManageDate> dateBirthday = new ArrayList<>();
        dateBirthday.add(this.setNotificationManageDate(30, 9, null, null, null));
        //notification.fees
        List<NotificationManageDate> dateFees = new ArrayList<>();
        dateFees.add(this.setNotificationManageDate(0, 8, 5, null, null));
        dateFees.add(this.setNotificationManageDate(0, 7, 10, null, null));
        dateFees.add(this.setNotificationManageDate(0, 15, 6, null, null));
        //thong bao mới
        List<NotificationManageDate> dateHomeNew1 = new ArrayList<>();
        dateHomeNew1.add(this.setNotificationManageDate(0, 14, null, null, null));
        List<NotificationManageDate> dateHomeNew2 = new ArrayList<>();
        dateHomeNew2.add(this.setNotificationManageDate(0, 14, null, null, null));
        List<NotificationManageDate> dateHomeNew3 = new ArrayList<>();
        dateHomeNew3.add(this.setNotificationManageDate(0, 14, null, null, null));
        List<NotificationManageDate> dateHomeNew4 = new ArrayList<>();
        dateHomeNew4.add(this.setNotificationManageDate(0, 14, null, null, null));
        List<NotificationManageDate> dateHomeNew5 = new ArrayList<>();
        dateHomeNew5.add(this.setNotificationManageDate(0, 14, null, null, null));
        List<NotificationManageDate> dateHomeNew6 = new ArrayList<>();
        dateHomeNew6.add(this.setNotificationManageDate(0, 14, null, null, null));
        //Th4ng báo học phí
        List<NotificationManageDate> thongBaoHocPhi = new ArrayList<>();
        thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 10, null, 30));
        thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 15, null, 50));
        thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 20, null, 70));
        thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 25, null, 85));
        thongBaoHocPhi.add(this.setNotificationManageDate(0, 16, 28, null, 100));
        //Th4ng báo c4ng luong
        List<NotificationManageDate> thongBaoCongLuong = new ArrayList<>();
        thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 10, null, 30));
        thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 15, null, 50));
        thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 20, null, 70));
        thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 25, null, 85));
        thongBaoCongLuong.add(this.setNotificationManageDate(0, 16, 28, null, 100));
        //Thu chi
        List<NotificationManageDate> thongBaoThuChi = new ArrayList<>();
        thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 10, null, null));
        thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 15, null, null));
        thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 20, null, null));
        thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 25, null, null));
        thongBaoThuChi.add(this.setNotificationManageDate(0, 9, 28, null, null));
        //===========================
        //Điểm danh đến
        this.createNotificationManage("Thông báo chưa điểm danh đến", "Có {number} học sinh trong lớp {class_name} chưa được điểm danh đến.", NotificationConstant.NOTIFICATION_ATTENDANCE_ARRIVE, AppTypeConstant.TEACHER, 0, idSchool, dateAttendanceArrive1);
        this.createNotificationManage("Thông báo chưa điểm danh đến", "Học sinh {kid_name} chưa được điểm danh đến.", NotificationConstant.NOTIFICATION_ATTENDANCE_ARRIVE, AppTypeConstant.PARENT, 0, idSchool, dateAttendanceArrive);
        //Điểm danh về
        this.createNotificationManage("Thông báo chưa điểm danh về", "Có {number} học sinh trong lớp {class_name} chưa được điểm danh về.", NotificationConstant.NOTIFICATION_ATTENDANCE_LEAVE, AppTypeConstant.TEACHER, 0, idSchool, dateAttendanceLeave1);
        this.createNotificationManage("Thông báo chưa điểm danh về", "Học sinh {kid_name} chưa được điểm danh về.", NotificationConstant.NOTIFICATION_ATTENDANCE_LEAVE, AppTypeConstant.PARENT, 0, idSchool, dateAttendanceLeave);
        //Điểm danh ăn
        this.createNotificationManage("Thông báo chưa điểm danh ăn", "Có {number} học sinh trong lớp {class_name} chưa được điểm danh ăn.", NotificationConstant.NOTIFICATION_ATTENDANCE_EAT, AppTypeConstant.TEACHER, 0, idSchool, dateAttendanceEat);
        //Dặn thuốc teacher
        this.createNotificationManage("Thông báo dặn thuốc chưa xác nhận", "Có {number} đơn thuốc trong lớp {class_name} chưa xác nhận.", NotificationConstant.NOTIFICATION_MEDICINE, AppTypeConstant.TEACHER, 0, idSchool, dateMedicine);
        //Lời nhắn teacher
        this.createNotificationManage("Thông báo lời nhắn chưa xác nhận", "Có {number} lời nhắn trong lớp {class_name} chưa xác nhận.", NotificationConstant.NOTIFICATION_MESSAGE, AppTypeConstant.TEACHER, 0, idSchool, dateMessageParent);
        //Xin nghỉ teacher
        this.createNotificationManage("Thông báo xin nghỉ chưa xác nhận", "Có {number} xin nghỉ trong lớp {class_name} chưa xác nhận.", NotificationConstant.NOTIFICATION_ABSENT, AppTypeConstant.TEACHER, 0, idSchool, dateAbsent);
        //Sinh nhật
        this.createNotificationManage("Thông báo sinh nhật học sinh", "Có {number} học sinh trong lớp {class_name} sinh nhật hôm nay.", NotificationConstant.NOTIFICATION_BIRTHDAY, AppTypeConstant.TEACHER, 0, idSchool, dateBirthday);
        //Hóa đơn học phí parent
        this.createNotificationManage("Thông báo chưa hoàn thành học phí", "Học sinh {kid_name} chưa hoàn thành học phí tháng {month}.", NotificationConstant.NOTIFICATION_FEES, AppTypeConstant.PARENT, 0, idSchool, dateFees);
        //Tổng hợp home parent
        this.createNotificationManage("", "- {number} lời nhắn mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew1);
        this.createNotificationManage("", "- {number} đơn dặn thuốc mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew2);
        this.createNotificationManage("", "- {number} đơn xin nghỉ mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew3);
        this.createNotificationManage("", "- {number} album ảnh mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew4);
        this.createNotificationManage("", "- {number} nhật xét mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew5);
        this.createNotificationManage("", "- {number} góp ý mới", NotificationConstant.NOTIFICATION_HOME, AppTypeConstant.PARENT, 0, idSchool, dateHomeNew6);
        //Thông báo học phí plus
        this.createNotificationManage("Thông báo học phí tháng {month}", "Chi tiết học phí đến ngày {date}\n" +
                "- Tổng tiền học phí: {moneyTotal} VNĐ\n" +
                "- Tổng tiền đã thu:  {moneyPaid} VNĐ\n" +
                "- Tổng tiền chưa thu:  {moneyNoPaid} VNĐ\n" +
                "- Số học sinh đã hoàn thành hóa đơn: {numberSuccess}\n" +
                "- Số học sinh chưa hoàn thành hóa đơn: {numberNoSuccess}", NotificationConstant.NOTIFICATION_FEES, AppTypeConstant.SCHOOL, 0, idSchool, thongBaoHocPhi);//Thông báo học phí
        //Thông báo công lương plus
        this.createNotificationManage("Thông báo công lương tháng {month}", "Chi tiết công lương đến ngày {date}\n" +
                "- Tổng tiền công lương cần chi: {moneyTotal} VNĐ\n" +
                "- Tổng tiền đã chi: {moneyPaid} VNĐ\n" +
                "- Tổng tiền chưa chi: {moneyNoPaid} VNĐ\n" +
                "- Số nhân sự đã hoàn thành hóa đơn: {numberSuccess}\n" +
                "- Số nhân sự chưa hoàn thành hóa đơn: {numberNoSuccess}", NotificationConstant.NOTIFICATION_SALARY, AppTypeConstant.SCHOOL, 0, idSchool, thongBaoCongLuong);
        //Thông báo thu chi nội bộ plus
        this.createNotificationManage("Thông báo thu chi nội bộ tháng {month}", "Tổng tiền thu chi nội bộ tính đến ngày {date}\n" +
                "- Tổng tiền phiếu thu: {moneyIn} VNĐ\n" +
                "- Tổng tiền phiếu chi: {moneyOut} VNĐ", NotificationConstant.NOTIFICATION_CASH_INTERNAL, AppTypeConstant.SCHOOL, 0, idSchool, thongBaoThuChi);
    }
}



