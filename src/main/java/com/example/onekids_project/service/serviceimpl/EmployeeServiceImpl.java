package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.dto.InfoEmployeeSchoolDTO;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.*;
import com.example.onekids_project.entity.school.ConfigAttendanceEmployeeSchool;
import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.*;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.importexport.model.EmployeeModelImport;
import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.mapper.EmployeeMapper;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.employee.UpdateEmployeeAdminRequest;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.common.StatusCommonRequest;
import com.example.onekids_project.request.createnotifyschool.CreateEmployeeNotify;
import com.example.onekids_project.request.department.TabDepartmentRequest;
import com.example.onekids_project.request.department.TabProfessionalRequest;
import com.example.onekids_project.request.employee.*;
import com.example.onekids_project.request.kids.AppIconTeacherRequest;
import com.example.onekids_project.request.kids.SmsStudentRequest;
import com.example.onekids_project.request.smsNotify.SMSRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyRequest;
import com.example.onekids_project.response.accounttype.AccountTypeOtherResponse;
import com.example.onekids_project.response.department.TabDepartmentResponse;
import com.example.onekids_project.response.employee.*;
import com.example.onekids_project.response.phone.AccountLoginResponse;
import com.example.onekids_project.response.phone.PhoneResponse;
import com.example.onekids_project.response.school.ListAppIconTeacherResponse;
import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.AccountCreateData;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.dto.sms.SmsDTO;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.service.servicecustom.common.FindSmsService;
import com.example.onekids_project.service.servicecustom.common.PhoneCommonService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.service.servicecustom.sms.SmsService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExEmployeeClassRepository exEmployeeClassRepository;

    @Autowired
    private ExDepartmentEmployeeRepository exDepartmentEmployeeRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private AppIconTeacherService appIconTeacherService;

    @Autowired
    private AppIconTeacherAddSerivce appIconTeacherAddSerivce;

    @Autowired
    private AppIconPlusService appIconPlusService;

    @Autowired
    private SmsSendCustomRepository smsSendCustomRepository;

    @Autowired
    private AppIconPlusAddSerivce appIconPlusAddSerivce;

    @Autowired
    private AppIconNotifyPlusService appIconNotifyPlusService;

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private SmsSendRepository smsSendRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsCodeRepository smsCodeRepository;

    @Autowired
    private SmsReiceiversRepository smsReiceiversRepository;

    @Autowired
    private SmsDataService smsDataService;

    @Autowired
    private PhoneCommonService phoneCommonService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SysInforRepository sysInforRepository;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private FindSmsService findSmsService;

    @Autowired
    private SmsReceiversCustomRepository smsReceiversCustomRepository;

    @Autowired
    private EmployeeStatusTimelineRepository employeeStatusTimelineRepository;

    @Autowired
    private ConfigAttendanceEmployeeSchoolRepository configAttendanceEmployeeSchoolRepository;

    @Autowired
    private ConfigAttendanceEmployeeRepository configAttendanceEmployeeRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GroupOutEmployeeRepository groupOutEmployeeRepository;

    private Long currentMilistime;

    private String fileNameAvatar;


    @Override
    public boolean createAccountAndEmployeeForOther(InfoEmployeeSchool infoEmployeeSchool) {
        String appType = infoEmployeeSchool.getAppType();
        String usernameEndExtend = ConvertData.getUsernameIncludeExtend(infoEmployeeSchool.getPhone(), appType);
        Optional<MaUser> maUserOptional = maUserRepository.findByUsername(usernameEndExtend);
        if (maUserOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_EXIST);
        }
        this.createAccountAndEmployee(infoEmployeeSchool);
        return true;
    }

    @Override
    public InfoEmployeeSchoolDTO findByIdEmployee(Long id) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        InfoEmployeeSchoolDTO infoEmployeeSchoolDTO = modelMapper.map(infoEmployeeSchool, InfoEmployeeSchoolDTO.class);
//        if (infoEmployeeSchool.getAccountType() != null) {
//            infoEmployeeSchoolDTO.setAccountType(infoEmployeeSchool.getAccountType().split(","));
//        }
        if (StringUtils.isNotBlank(infoEmployeeSchool.getAppType())) {
            infoEmployeeSchoolDTO.setAppType(infoEmployeeSchool.getAppType());
        }
        List<AccountType> accountTypeList = infoEmployeeSchool.getAccountTypeList();
        infoEmployeeSchoolDTO.setIdAccountTypeList(accountTypeList.stream().map(x -> x.getId()).collect(Collectors.toList()));
        infoEmployeeSchoolDTO.setAvatarView(infoEmployeeSchool.getAvatar());
        return infoEmployeeSchoolDTO;
    }

    @Override
    @Transactional
    public boolean createEmployee(CreateEmployeeRequest createEmployeeRequest, UserPrincipal principal) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        //Xử lý Tab thông tin nhân viên khi Create Employee
        School school = schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE).orElseThrow(() -> new RuntimeException("Fail! -> Không tìm thấy school có id=" + idSchool));
        InfoEmployeeSchool infoEmployeeSchool = modelMapper.map(createEmployeeRequest.getCreateEmployeeMainInfoRequest(), InfoEmployeeSchool.class);
        this.setFullNameEmployee(infoEmployeeSchool, infoEmployeeSchool.getFullName());
        if (StringUtils.isNotBlank(createEmployeeRequest.getCreateEmployeeMainInfoRequest().getAvatar())) {
            getCurrentTimeMilisInitial();
            fileNameAvatar = HandleFileUtils.removeSpace(currentMilistime + "_" + idSchool + "_" + createEmployeeRequest.getCreateEmployeeMainInfoRequest().getAvatar());

            String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.AVATAR) + fileNameAvatar;
            String urlLocal = HandleFileUtils.getUrl(SystemConstant.ID_SYSTEM, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR) + fileNameAvatar;

            String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR);
            infoEmployeeSchool.setAvatar(urlWeb);
            infoEmployeeSchool.setUrlLocalAvatar(urlFolder + fileNameAvatar);
        }

        infoEmployeeSchool.setEmployeeStatus(AppConstant.EMPLOYEE_STATUS_WORKING);

        infoEmployeeSchool.setSchool(school);
        infoEmployeeSchool.setCode(GenerateCode.codeEmployee());
        infoEmployeeSchool.setSmsReceive(AppConstant.APP_TRUE);
        infoEmployeeSchool.setSmsSend(AppConstant.APP_FALSE);
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        infoEmployeeSchool.setVerifyCodeSchool(GenerateCode.getNumber());
        infoEmployeeSchool.setVerifyCodeAdmin(sysInfor.getVerificationCode());
        InfoEmployeeSchool newInfoEmployeeSchool = infoEmployeeSchoolRepository.save(infoEmployeeSchool);

        this.createExtraInfoEmployee(infoEmployeeSchool);
        this.createAccountType(newInfoEmployeeSchool.getId(), createEmployeeRequest.getCreateEmployeeMainInfoRequest().getIdAccountTypeList());
        appIconTeacherAddSerivce.createAppIconTeacherAdd(idSchool, infoEmployeeSchool, createEmployeeRequest.getAppIconTeacherRequestList());
        appIconPlusAddSerivce.createAppIconPlusAdd(idSchool, infoEmployeeSchool, createEmployeeRequest.getAppIconPlusRequestList());
//        if (createEmployeeRequest.getCreateEmployeeMainInfoRequest().getAppType().equals(AppTypeConstant.SCHOOL)) {
//            appIconNotifyPlusService.createAppIconNotifyPlusAdd(idSchool, infoEmployeeSchool, createEmployeeRequest.getNotifyAppIconPlusRequestList());
//        }


        //Xử lý Tab Phòng ban khi Create Employee
        if (!CollectionUtils.isEmpty(createEmployeeRequest.getTabDepartmentRequestList())) {
            ExDepartmentEmployee exDepartmentEmployee = null;
            for (TabDepartmentRequest tabDepartmentRequest : createEmployeeRequest.getTabDepartmentRequestList()) {
                exDepartmentEmployee = new ExDepartmentEmployee();
                exDepartmentEmployee.setInfoEmployeeSchool(infoEmployeeSchool);
                exDepartmentEmployee.setPosition(tabDepartmentRequest.getPosition());
                exDepartmentEmployee.setDepartment(departmentRepository.findById(tabDepartmentRequest.getId()).get());
                exDepartmentEmployee = exDepartmentEmployeeRepository.save(exDepartmentEmployee);
            }
        }

        //Xử Lý Tab Chuyên môn khi Create Employee
        this.saveExEmployeeClass(infoEmployeeSchool, createEmployeeRequest.getTabProfessionalRequestList());
        //create account and employee
        this.createAccountAndEmployee(newInfoEmployeeSchool);
        this.setCreateIdClassLogin(createEmployeeRequest.getTabProfessionalRequestList(), infoEmployeeSchool.getId());
        CreateEmployeeMainInfoResponse createEmployeeMainInfoResponse = modelMapper.map(infoEmployeeSchool, CreateEmployeeMainInfoResponse.class);
        createEmployeeMainInfoResponse.setIdSchool(idSchool);
        CreateEmployeeResponse createEmployeeResponse = new CreateEmployeeResponse();
        createEmployeeResponse.setCreateEmployeeMainInfoResponse(createEmployeeMainInfoResponse);
        return true;
    }

    private void createAttendanceEmployee(InfoEmployeeSchool infoEmployeeSchool) {
        ConfigAttendanceEmployeeSchool configAttendanceEmployeeSchool = configAttendanceEmployeeSchoolRepository.findBySchoolId(infoEmployeeSchool.getSchool().getId()).orElseThrow(() -> new NoSuchElementException("not found configAttendanceEmployeeSchool by id in create employee"));
        ConfigAttendanceEmployee configAttendanceEmployee = modelMapper.map(configAttendanceEmployeeSchool, ConfigAttendanceEmployee.class);
        configAttendanceEmployee.setId(null);
        configAttendanceEmployee.setInfoEmployeeSchool(infoEmployeeSchool);
        configAttendanceEmployeeRepository.save(configAttendanceEmployee);
    }

    private void addRoleForEmployee(InfoEmployeeSchool infoEmployeeSchool) {
        if (AppTypeConstant.SCHOOL.equals(infoEmployeeSchool.getAppType())) {
            Role role1 = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(infoEmployeeSchool.getSchool().getId(), AppConstant.ROLE_COMMON).orElseThrow();
            MaUser maUser = infoEmployeeSchool.getEmployee().getMaUser();
            maUserRepository.insertUserRole(maUser.getId(), role1.getId());
            Role role2 = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(infoEmployeeSchool.getSchool().getId(), AppConstant.ROLE_FINANCE).orElseThrow();
            maUserRepository.insertUserRole(maUser.getId(), role2.getId());
        } else if (AppTypeConstant.TEACHER.equals(infoEmployeeSchool.getAppType())) {
            Role role = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(infoEmployeeSchool.getSchool().getId(), AppConstant.ROLE_TEACHER).orElseThrow();
            MaUser maUser = infoEmployeeSchool.getEmployee().getMaUser();
            maUserRepository.insertUserRole(maUser.getId(), role.getId());
        }
    }

    private void createExtraInfoEmployee(InfoEmployeeSchool infoEmployeeSchool) {
        EmployeeStatusTimeline entity = new EmployeeStatusTimeline();
        entity.setStartDate(LocalDate.now());
        entity.setStatus(infoEmployeeSchool.getEmployeeStatus());
        entity.setInfoEmployeeSchool(infoEmployeeSchool);
        employeeStatusTimelineRepository.save(entity);

        // tạo điểm danh nhân sự
        this.createAttendanceEmployee(infoEmployeeSchool);
    }

    private void deleteEmployeeClass(Long idInfoEmployee) {
        List<ExEmployeeClass> exEmployeeClassList = exEmployeeClassRepository.findByInfoEmployeeSchoolId(idInfoEmployee);
        exEmployeeClassList.forEach(x -> {
            exEmployeeClassRepository.deleteExEmployeeClassSubject(x.getId());
        });
        exEmployeeClassRepository.deleteByInfoEmployeeSchoolId(idInfoEmployee);
    }

    private void saveExEmployeeClass(InfoEmployeeSchool infoEmployeeSchool, List<TabProfessionalRequest> requestList) {
        if (!CollectionUtils.isEmpty(requestList)) {
            requestList.forEach(x -> {
                ExEmployeeClass exEmployeeClass = new ExEmployeeClass();
                MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
                if (x.getIsMaster() != null) {
                    exEmployeeClass.setMaster(x.getIsMaster());
                }
                exEmployeeClass.setInfoEmployeeSchool(infoEmployeeSchool);
                exEmployeeClass.setMaClass(maClass);
                ExEmployeeClass exEmployeeClassSaved = exEmployeeClassRepository.save(exEmployeeClass);
                this.saveExEmployeeClassSubject(exEmployeeClassSaved.getId(), x.getListIdSubject());
            });
        }
    }

    private void setFullNameEmployee(InfoEmployeeSchool infoEmployeeSchool, String fullName) {
        infoEmployeeSchool.setFirstName(CommonUtil.convertFistName(fullName));
        infoEmployeeSchool.setLastName(CommonUtil.convertLastName(fullName));
        infoEmployeeSchool.setFullName(CommonUtil.getFullName(fullName));
    }

    private void saveExEmployeeClassSubject(Long idExEmployeeClass, List<Long> idSubjectList) {
        if (!CollectionUtils.isEmpty(idSubjectList)) {
            idSubjectList.forEach(x -> {
                exEmployeeClassRepository.insertExEmployeeClassSubject(idExEmployeeClass, x);
            });
        }
    }

    private void createAccountType(Long idInfoEmployee, List<Long> idAccountTypeList) {
        if (!CollectionUtils.isEmpty(idAccountTypeList)) {
            infoEmployeeSchoolRepository.deleteEmployeeAccountType(idInfoEmployee);
            idAccountTypeList.forEach(x -> {
                infoEmployeeSchoolRepository.insertEmployeeAccountType(idInfoEmployee, x);
            });
        }
    }


    @Override
    public void uploadAvatarEmployee(Long idSchool, MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR);
            String fileName = fileNameAvatar;
//            String fileName = HandleFileUtils.removeSpace(currentMilistime + "_" + idSchool + "_" + multipartFile.getOriginalFilename());
//            this.currentMilistime = null;
//            String fileNameResize = "thumbnail_" + fileName;
//            Path pathUploadAvatar = Paths.get(urlFolder, fileName);
//            File file1 = new File(urlFolder);
//            if (!file1.exists()) {
//                file1.mkdirs();
//            }
//            Files.write(pathUploadAvatar, multipartFile.getBytes());
//            int scaledHeightNewInt = calculateHeightNew(urlFolder + fileName);
//            resize(urlFolder + fileName, urlFolder + fileNameResize, 512, scaledHeightNewInt);
//            Files.write(pathUploadAvatar, multipartFile.getBytes());
            HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);

        }
        this.fileNameAvatar = null;
    }

    @Override
    @Transactional
    public boolean updateEmployee(UserPrincipal principal, Long idUrl, UpdateEmployeeRequest updateEmployeeRequest) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        CommonValidate.checkMatchIdUrlWithBody(idUrl, updateEmployeeRequest.getUpdateEmployeeMainInfoRequest().getId());
        Long idSchool = principal.getIdSchoolLogin();
        InfoEmployeeSchool oldInfoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(updateEmployeeRequest.getUpdateEmployeeMainInfoRequest().getId(), idSchool).orElseThrow();
        UpdateEmployeeMainInfoRequest updateEmployeeMainInfoRequest = updateEmployeeRequest.getUpdateEmployeeMainInfoRequest();
        this.setDateStatus(updateEmployeeMainInfoRequest);
        //tạo time line trạng thái nhân sự
        LocalDate newDate = this.getDateWithEmployeeStatus(updateEmployeeMainInfoRequest.getEmployeeStatus(), updateEmployeeMainInfoRequest.getDateRetain(), updateEmployeeMainInfoRequest.getDateLeave());
        this.updateOrCreateEmployeeStatusTimeline(oldInfoEmployeeSchool, updateEmployeeMainInfoRequest.getEmployeeStatus(), newDate);
        modelMapper.map(updateEmployeeRequest.getUpdateEmployeeMainInfoRequest(), oldInfoEmployeeSchool);
        this.createAccountType(updateEmployeeRequest.getUpdateEmployeeMainInfoRequest().getId(), updateEmployeeRequest.getUpdateEmployeeMainInfoRequest().getIdAccountTypeList());
        InfoEmployeeSchool newInfoEmployeeSchool = infoEmployeeSchoolRepository.save(oldInfoEmployeeSchool);
        this.setFullNameEmployee(oldInfoEmployeeSchool, updateEmployeeRequest.getUpdateEmployeeMainInfoRequest().getFullName());
        if (newInfoEmployeeSchool.getAppType().equals(AppTypeConstant.TEACHER)) {
            appIconTeacherAddSerivce.updateAppIconTeacherAdd(idSchool, newInfoEmployeeSchool, updateEmployeeRequest.getAppIconTeacherRequestList());
        } else if (newInfoEmployeeSchool.getAppType().equals(AppTypeConstant.SCHOOL)) {
            appIconPlusAddSerivce.updateAppIconPlusAdd(idSchool, newInfoEmployeeSchool, updateEmployeeRequest.getAppIconPlusRequestList());
//            appIconNotifyPlusService.updateAppIconNotifyPlusAdd(idSchool, newInfoEmployeeSchool, updateEmployeeRequest.getNotifyAppIconPlusRequestList());
        }


        //Xử lý Tab Phòng ban khi Update Employee
        if (updateEmployeeRequest.getTabDepartmentRequestList() != null) {
            /* xóa các bản ghi cũ */
            exDepartmentEmployeeRepository.deleteByInfoEmployeeSchoolId(newInfoEmployeeSchool.getId());
            /*Cập nhật các bản ghi*/
            ExDepartmentEmployee exDepartmentEmployee = null;
            for (TabDepartmentRequest tabDepartmentRequest : updateEmployeeRequest.getTabDepartmentRequestList()) {
                exDepartmentEmployee = new ExDepartmentEmployee();
                exDepartmentEmployee.setInfoEmployeeSchool(newInfoEmployeeSchool);
                exDepartmentEmployee.setPosition(tabDepartmentRequest.getPosition());
                exDepartmentEmployee.setDepartment(departmentRepository.findById(tabDepartmentRequest.getId()).get());
                exDepartmentEmployee.setDelActive(true);
                exDepartmentEmployeeRepository.save(exDepartmentEmployee);
            }
        }

        //Xử lý Tab Phân công chuyên môn
        this.deleteEmployeeClass(newInfoEmployeeSchool.getId());
        this.saveExEmployeeClass(newInfoEmployeeSchool, updateEmployeeRequest.getTabProfessionalRequestList());
        //nếu chưa có tài khoản thì tạo tài khoản
        this.createAccountNotExist(newInfoEmployeeSchool);
        this.clearApartmentAndClass(newInfoEmployeeSchool.getId(), updateEmployeeMainInfoRequest.getEmployeeStatus());

        //update class khi có sự thay đổi calsss
        if (newInfoEmployeeSchool.getEmployee() == null) {
            this.setCreateIdClassLogin(updateEmployeeRequest.getTabProfessionalRequestList(), newInfoEmployeeSchool.getId());
        } else {
            //cập nhật lại idClassLogin
            this.setUpdateIdClassLogin(updateEmployeeRequest.getTabProfessionalRequestList(), newInfoEmployeeSchool.getId());
        }
        return true;
    }


    @Transactional
    @Override
    public boolean updateEmployeeAdmin(UpdateEmployeeAdminRequest updateEmployeeRequest) {
        InfoEmployeeSchool oldInfoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(updateEmployeeRequest.getUpdateEmployeeMainInfoRequest().getId()).orElseThrow();
        modelMapper.map(updateEmployeeRequest.getUpdateEmployeeMainInfoRequest(), oldInfoEmployeeSchool);
//        this.setAccountType(updateEmployeeRequest.getUpdateEmployeeMainInfoRequest(), oldInfoEmployeeSchool);
        InfoEmployeeSchool newInfoEmployeeSchool = infoEmployeeSchoolRepository.save(oldInfoEmployeeSchool);
        this.createAccountNotExist(newInfoEmployeeSchool);
        return true;
    }


    @Override
    public void uploadAvatarEditEmployee(Long idSchool, MultipartFile multipartFile, Long idEmployee, String fileNames) throws IOException {
        if (multipartFile != null) {
            Optional<InfoEmployeeSchool> infoEmployeeSchoolOptional = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(idEmployee, idSchool);
            if (infoEmployeeSchoolOptional.isEmpty()) {
                return;
            }
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolOptional.get();
            this.getCurrentTimeMilisInitial();
            if (StringUtils.isNotBlank(infoEmployeeSchool.getUrlLocalAvatar())) {
                String pathAvatarInfoEmployeeSchoool = infoEmployeeSchool.getUrlLocalAvatar();
                File fileDelete = new File(pathAvatarInfoEmployeeSchoool);
                boolean checkDelete = fileDelete.delete();
//                if(!checkDelete){
//                    return;
//                }
                File fileDeleteThumbnail = new File(pathAvatarInfoEmployeeSchoool.replace("avatar\\\\", "avatar\\\\thumbnail_"));
                boolean checkDeleteThumbnail = fileDeleteThumbnail.delete();
//                if(!checkDeleteThumbnail){
//                    return;
//                }
            }

            if (multipartFile != null) {

//                String uploadFileSystem = AppConstant.UPLOAD_SYSFILES;
//                String fileName = HandleFileUtils.removeSpace(currentMilistime + "_" + idSchool + "_" + multipartFile.getOriginalFilename());
//                String fileNameResize = "thumbnail_" + fileName;
//                Path pathUploadAvatar = Paths.get(uploadFileSystem, fileName);
//                File file1 = new File(uploadFileSystem);
//                if (!file1.exists()) {
//                    file1.mkdirs();
//                }
//                Files.write(pathUploadAvatar, multipartFile.getBytes());
//                int scaledHeightNewInt = calculateHeightNew(uploadFileSystem + fileName);
//                resize(uploadFileSystem + fileName, uploadFileSystem + fileNameResize, 512, scaledHeightNewInt);
//                Files.write(pathUploadAvatar, multipartFile.getBytes());
                getCurrentTimeMilisInitial();


                fileNameAvatar = HandleFileUtils.removeSpace(currentMilistime + "_" + idSchool + "_" + multipartFile.getOriginalFilename());

                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.AVATAR) + fileNameAvatar;

                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR);

                HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileNameAvatar, UploadDownloadConstant.WIDTH_OTHER);

                infoEmployeeSchool.setAvatar(urlWeb);
                infoEmployeeSchool.setUrlLocalAvatar(urlFolder + fileNameAvatar);
                infoEmployeeSchoolRepository.save(infoEmployeeSchool);
                this.fileNameAvatar = null;

            }
        }

    }

    @Transactional
    @Override
    public boolean createEmployeeNotify(UserPrincipal principal, CreateEmployeeNotify createEmployeeNotify) throws IOException, FirebaseMessagingException {
        List<InfoEmployeeSchool> infoEmployeeSchools = null;
        Long idSchool = principal.getIdSchoolLogin();
        if (!CollectionUtils.isEmpty(createEmployeeNotify.getDataDepartmentNotifyList())) {
            infoEmployeeSchools = infoEmployeeSchoolRepository.findInfoEmployeeDeparmentList(principal.getIdSchoolLogin(), createEmployeeNotify.getDataDepartmentNotifyList());
        }

        if (!CollectionUtils.isEmpty(createEmployeeNotify.getDataEmployeeNotifyList())) {
            infoEmployeeSchools = infoEmployeeSchoolRepository.findInfoEmployeeSchool(principal.getIdSchoolLogin(), createEmployeeNotify.getDataEmployeeNotifyList());

        }
        if (CollectionUtils.isEmpty(infoEmployeeSchools)) {
            return false;
        }

        AppSend appSend = modelMapper.map(createEmployeeNotify, AppSend.class);
        if (!CollectionUtils.isEmpty(infoEmployeeSchools)) {
            appSend.setReceivedNumber(infoEmployeeSchools.size());
        }
        appSend.setIdSchool(idSchool);
        appSend.setSendType(AppSendConstant.TYPE_COMMON);
        appSend.setAppType(principal.getAppType());
        appSend.setCreatedBy(principal.getFullName());
        appSend.setTypeReicever(AppTypeConstant.TEACHER);
        appSend.setApproved(AppConstant.APP_TRUE);
        appSend.setTimeSend(LocalDateTime.now());
        appSend = appSendRepository.save(appSend);

        if (!CollectionUtils.isEmpty(infoEmployeeSchools)) {
            AppSend finalAppSend1 = appSend;
            infoEmployeeSchools.forEach(info -> {
                Receivers receivers = new Receivers();
                if (info.getEmployee().getMaUser() != null) {
                    receivers.setIdUserReceiver(info.getEmployee().getMaUser().getId());
                }
                receivers.setIdSchool(idSchool);
                receivers.setUserUnread(AppConstant.APP_FALSE);
                receivers.setCreatedBy(principal.getFullName());
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setAppSend(finalAppSend1);
                receiversRepository.save(receivers);
            });
        }
        List<UrlFileAppSend> urlFileAppSendList = new ArrayList<>();
        int countAttachFile = 0;
        int countAttachPicture = 0;
        if (createEmployeeNotify.getMultipartFileList() != null && createEmployeeNotify.getMultipartFileList().length > 0) {
            for (MultipartFile multipartFile : createEmployeeNotify.getMultipartFileList()) {

                String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.THONG_BAO);
                String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
                HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);

                String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.THONG_BAO) + fileName;
                String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.THONG_BAO) + fileName;
                String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());


                UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
                if (extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                    urlFileAppSend.setAttachPicture(urlWeb);
                    urlFileAppSend.setUrlLocalPicture(urlLocal);
                    urlFileAppSend.setName(multipartFile.getOriginalFilename());
                    countAttachPicture++;
                } else {
                    urlFileAppSend.setAttachFile(urlWeb);
                    urlFileAppSend.setUrlLocalFile(urlLocal);
                    urlFileAppSend.setName(multipartFile.getOriginalFilename());
                    countAttachFile++;
                }
                urlFileAppSend.setCreatedBy(principal.getFullName());
                urlFileAppSend.setAppSend(appSend);
                urlFileAppSendRepository.save(urlFileAppSend);
            }
        }

        for (InfoEmployeeSchool x : infoEmployeeSchools) {
            //gửi firebase
            firebaseFunctionService.sendTeacherByPlus(4L, x, createEmployeeNotify.getSendTitle(), FirebaseConstant.CATEGORY_NOTIFY, idSchool);
        }
        return true;
    }

    @Override
    public boolean createEmployeeNotifySms(UserPrincipal principal, SmsNotifyRequest smsNotifyRequest) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Employee> employees;
        if (!CollectionUtils.isEmpty(smsNotifyRequest.getDataEmployeeNotifySmsList())) {
            employees = employeeRepository.findAllEmployeeList(smsNotifyRequest.getDataEmployeeNotifySmsList());
        } else if (!CollectionUtils.isEmpty(smsNotifyRequest.getDataDepartmentNotifySmsList())) {
            employees = employeeRepository.findAllEmployeeList(smsNotifyRequest.getDataDepartmentNotifySmsList());
        } else {
            throw new NotFoundException("Không có giáo viên nào được chọn");
        }
        List<PhoneResponse> phoneResponseList = phoneCommonService.findPhoneEmpolyess(employees);
        List<String> phoneSms = new ArrayList<>();
        phoneResponseList.forEach(phone -> {
            phoneSms.add(phone.getPhone());
        });

        List<MaUser> parentList = employees.stream().map(Employee::getMaUser).collect(Collectors.toList());

        SmsDTO smsDTO = getSmsDTOByShoolId(principal.getIdSchoolLogin());
        SmsSend smsSend = modelMapper.map(smsNotifyRequest, SmsSend.class);
        int smsNumber = phoneResponseList.size();
        if (!CollectionUtils.isEmpty(phoneResponseList)) {
            smsSend.setSmsNumber(smsNumber);
        }
        smsSend.setTitleContent(smsNotifyRequest.getSendTitle());
        smsSend.setId_school(principal.getIdSchoolLogin());
        smsSend.setSendType(SmsConstant.TYPE_SMS);
        smsSend.setAppType(principal.getAppType());
        smsSend.setServiceProvider(smsDTO.getSupplierCode());

        String contentSms;
        if (!smsNotifyRequest.getSendTitle().isEmpty()) {
            contentSms = smsNotifyRequest.getSendTitle() + " " + smsNotifyRequest.getSendContent();
        } else contentSms = smsNotifyRequest.getSendContent();

        SmsConvertResponse smsConvertResponse = findSmsService.convertSms(principal, contentSms);
        int totalSend = smsConvertResponse.getSmsConvert().size() * smsNumber;
        smsSend.setSmsTotal(totalSend);


        if (smsNotifyRequest.isTimer() == true && smsNotifyRequest.getDateTime() != null) {

            smsSend.setTimeAlarm(smsNotifyRequest.getDateTime());
            smsSend.setSent(AppConstant.APP_FALSE);

            smsSend = smsSendRepository.save(smsSend);
            SmsSend finalSmsSend = smsSend;
            parentList.forEach(parent -> {
                SmsReceivers smsReceivers = new SmsReceivers();
                smsReceivers.setIdUserReceiver(parent.getId());
                smsReceivers.setPhone(parent.getPhone());
                smsReceivers.setIdSchool(parent.getEmployee().getIdSchoolLogin());
                smsReceivers.setSmsSend(finalSmsSend);
                smsReiceiversRepository.save(smsReceivers);
            });
        } else {
            Future<List<SmsResultDTO>> rs = smsService.sendSms(principal.getIdSchoolLogin(), phoneSms, contentSms);

            if (rs.get() != null) {
                smsSend.setSent(AppConstant.APP_TRUE);
                smsSend = smsSendRepository.save(smsSend);
                List<SmsResultDTO> smsResultDTOS = rs.get();
                SmsSend finalSmsSend = smsSend;
                List<Employee> finalEmployees = employees;
                smsResultDTOS.forEach(result -> {
                    String phoneCv = result.getPhone();
                    String cutPhone = phoneCv.substring(0, 1);
                    if (cutPhone.equalsIgnoreCase("84")) {
                        phoneCv = "0".concat(phoneCv.substring(2));
                    }
                    List<Employee> employees1 = this.getEmployeeByPhone(phoneCv, finalEmployees);
                    Employee employee = employees1.get(0);
                    SmsReceivers smsReceivers = new SmsReceivers();
                    smsReceivers.setIdUserReceiver(employee.getId());
                    smsReceivers.setPhone(phoneCv);
                    smsReceivers.setIdSchool(employee.getIdSchoolLogin());
                    Optional<SmsCode> smsCode = smsCodeRepository.findById(result.getErrCodeId());
                    smsReceivers.setSmsCode(smsCode.get());
                    smsReceivers.setSmsSend(finalSmsSend);
                    smsReiceiversRepository.save(smsReceivers);

                });
            }
        }
        return true;
    }

    @Override
    public boolean sendSmsInfoEmployee(UserPrincipal principal, SMSRequest request) throws ExecutionException, InterruptedException {
        SmsNotifyDataRequest smsNotifyDataRequest = modelMapper.map(request, SmsNotifyDataRequest.class);
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByIdInAndSchoolIdAndDelActiveTrue(request.getIdList(), principal.getIdSchoolLogin());
        smsNotifyDataRequest.setSendContent(request.getSendContent());
        smsDataService.sendSmsEmployee(infoEmployeeSchoolList, principal.getIdSchoolLogin(), smsNotifyDataRequest, AppTypeConstant.SCHOOL);
        return true;
    }

    @Override
    public boolean createEmployeeSms(UserPrincipal principal, SmsStudentRequest request) throws ExecutionException, InterruptedException {
        List<InfoEmployeeSchool> dataList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        if (SmsConstant.SMS_EMPLOYEE.equals(request.getType())) {
            dataList = infoEmployeeSchoolRepository.findByIdInAndSchoolIdAndDelActiveTrue(request.getIdList(), idSchool);
        } else if (SmsConstant.SMS_DEPARTMENT.equals(request.getType())) {
            List<Department> departmentList = departmentRepository.findByIdInAndSchoolIdAndDelActiveTrue(request.getIdList(), idSchool);
            for (Department x : departmentList) {
                List<InfoEmployeeSchool> infoEmployeeSchoolList = x.getDepartmentEmployeeList().stream().map(ExDepartmentEmployee::getInfoEmployeeSchool).collect(Collectors.toList());
                dataList.addAll(infoEmployeeSchoolList);
            }
            dataList = dataList.stream().filter(x -> x.isDelActive() && x.getEmployeeStatus().equals(EmployeeConstant.STATUS_WORKING)).distinct().collect(Collectors.toList());
        }
        SmsNotifyDataRequest smsNotifyDataRequest = modelMapper.map(request, SmsNotifyDataRequest.class);
        smsDataService.sendSmsEmployee(dataList, idSchool, smsNotifyDataRequest, AppTypeConstant.SCHOOL);
        return true;
    }

    @Transactional
    @Override
    public List<EmployeeModelImport> saveDataEmployeeExcel(List<EmployeeModelImport> employeeModels, UserPrincipal principal) {
        List<EmployeeModelImport> employeeModelImportList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        for (EmployeeModelImport model : employeeModels) {
            try {
                CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
                List<TabDepartmentRequest> tabDepartmentRequestList = new ArrayList<>();
                List<TabProfessionalRequest> tabProfessionalRequests = new ArrayList<>();
                List<AppIconTeacherRequest> appIconTeacherRequests = new ArrayList<>();


                CreateEmployeeMainInfoRequest createEmployeeMainInfoRequest = modelMapper.map(model, CreateEmployeeMainInfoRequest.class);
                if (StringUtils.isNotEmpty(model.getCmndDate())) {
                    createEmployeeMainInfoRequest.setCmndDate(ConvertData.convertStringToDate(model.getCmndDate()));
                }
                if (StringUtils.isNotEmpty(model.getContractDate())) {
                    createEmployeeMainInfoRequest.setContractDate(ConvertData.convertStringToDate(model.getContractDate()));
                }
                if (StringUtils.isNotEmpty(model.getEndDate())) {
                    createEmployeeMainInfoRequest.setEndDate(ConvertData.convertStringToDate(model.getEndDate()));
                }
                createEmployeeMainInfoRequest.setBirthday(ConvertData.convertStringToDate(model.getBirthday()));
                createEmployeeMainInfoRequest.setStartDate(ConvertData.convertStringToDate(model.getStartDate()));
                createEmployeeMainInfoRequest.setEmployeeStatus(EmployeeConstant.STATUS_WORKING);
                createEmployeeMainInfoRequest.setAppType(AppTypeConstant.TEACHER);
                if (model.getMarriedStatus().equalsIgnoreCase("Độc thân") || model.getMarriedStatus().equalsIgnoreCase("Đã kết hôn")) {
                    createEmployeeMainInfoRequest.setMarriedStatus(model.getMarriedStatus());
                }
                if (!model.getObjectEmployee().isEmpty()) {
                    String objEmployee = model.getObjectEmployee().replace("\n", ",");
                    List<String> myList = new ArrayList<>(Arrays.asList(objEmployee.split(",")));
                    List<AccountType> accountTypeList = new ArrayList<>();
                    myList.forEach(x -> {
                        AccountType accountType = accountTypeRepository.findAllByDelActiveTrueAndNameAndAndIdSchool(x.trim(), idSchool);
                        if (accountType != null) {
                            accountTypeList.add(accountType);
                        }
                    });
                    List<Long> idType = accountTypeList.stream().map(BaseEntity::getId).collect(Collectors.toList());
                    createEmployeeMainInfoRequest.setIdAccountTypeList(idType);
                }

                if (!model.getNameDepartment().isEmpty()) {
                    String deparmentEmployee = model.getNameDepartment().replace("\n", ",");
                    List<String> myList = new ArrayList<String>(Arrays.asList(deparmentEmployee.split(",")));
                    myList.forEach(x -> {
                        Optional<Department> department = departmentRepository.findByDepartmentNameAndDelActiveTrueAndSchool_Id(x.trim(), idSchool);
                        if (department.isPresent()) {
                            TabDepartmentRequest tabDepartmentRequest = new TabDepartmentRequest();
                            tabDepartmentRequest.setId(department.get().getId());
                            tabDepartmentRequestList.add(tabDepartmentRequest);
                        }
                    });
                }
                if (!model.getProfessional().isEmpty()) {
                    String classEmployee = model.getProfessional().replace("\n", ",");
                    List<String> myList = new ArrayList<String>(Arrays.asList(classEmployee.split(",")));
                    myList.forEach(x -> {
                        Optional<MaClass> maClass = maClassRepository.findByClassNameAndDelActiveTrueAndIdSchool(x.trim(), idSchool);
                        if (maClass.isPresent()) {
                            TabProfessionalRequest tabProfessionalRequest = new TabProfessionalRequest();
                            tabProfessionalRequest.setId(maClass.get().getId());
                            tabProfessionalRequests.add(tabProfessionalRequest);
                        }
                    });
                }
                ListAppIconTeacherResponse listAppIconTeacherResponse = appIconTeacherService.getAppIconTeacher(idSchool);
                if (!listAppIconTeacherResponse.getAppIconTeacherResponseList().isEmpty()) {
                    listAppIconTeacherResponse.getAppIconTeacherResponseList().forEach(x -> {
                        AppIconTeacherRequest appIconTeacherRequest = new AppIconTeacherRequest();
                        appIconTeacherRequest.setIconName(x.getIconName());
                        appIconTeacherRequest.setStatus(AppConstant.APP_TRUE);
                        appIconTeacherRequests.add(appIconTeacherRequest);
                    });
                }
                createEmployeeRequest.setAppIconTeacherRequestList(appIconTeacherRequests);
                createEmployeeRequest.setCreateEmployeeMainInfoRequest(createEmployeeMainInfoRequest);
                createEmployeeRequest.setTabDepartmentRequestList(tabDepartmentRequestList);
                createEmployeeRequest.setTabProfessionalRequestList(tabProfessionalRequests);
                createEmployee(createEmployeeRequest, principal);
            } catch (Exception e) {
                employeeModelImportList.add(model);
                logger.info("Lỗi tạo dữ liệu nhân viên từ file excel", e);
                continue;
            }
        }
        return employeeModelImportList;
    }

    @Override
    public boolean sendAccountEmployeeSms(UserPrincipal principal, List<Long> idEmployee) throws ExecutionException, InterruptedException {
        List<Employee> employees = employeeRepository.findAllEmployeeList(idEmployee);
        List<AccountLoginResponse> accountEmployes = phoneCommonService.findAccountEmployess(employees);
        SmsDTO smsDTO = getSmsDTOByShoolId(principal.getIdSchoolLogin());
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(SmsTitleConstant.SEND_ACCOUNT_TEACHER).orElseThrow();
        int size = accountEmployes.size();

        SmsSendCustom smsSendCustom = new SmsSendCustom();
        smsSendCustom.setAppType(AppTypeConstant.SCHOOL);
        smsSendCustom.setIdSchool(principal.getIdSchoolLogin());
        smsSendCustom.setReceivedCount(size);
        smsSendCustom.setSendTitle(webSystemTitle.getTitle());
        smsSendCustom.setServiceProvider(smsDTO.getSupplierCode());
        smsSendCustom.setSendType(SmsConstant.TYPE_SEND_ACCOUNT);

        smsSendCustom = smsSendCustomRepository.save(smsSendCustom);


        for (AccountLoginResponse x : accountEmployes) {
            String contentSms = webSystemTitle.getTitle().concat(webSystemTitle.getContent());
            String[] parts = x.getUsername().split("#");
            contentSms = contentSms.replace("{username}", parts[0]);
            contentSms = contentSms.replace("{password}", x.getPasswordShow());
            SmsConvertResponse smsConvertResponse = findSmsService.convertSms(principal, contentSms);
            int totalSend = smsConvertResponse.getSmsConvert().size() * size;
            List<String> phoneSms = Arrays.asList(x.getPhone());
            Future<List<SmsResultDTO>> rs = smsService.sendSms(principal.getIdSchoolLogin(), phoneSms, contentSms);
            List<SmsResultDTO> smsResultDTOS = rs.get();
            SmsReceiversCustom smsReceiversCustom = new SmsReceiversCustom();
            smsResultDTOS.forEach(result -> {
                String phoneCv = SmsUtils.convertPhone(result.getPhone());
                Optional<SmsCode> smsCode = smsCodeRepository.findById(result.getErrCodeId());
                List<Employee> employees1 = this.getEmployeeByPhone(phoneCv, employees);
                if (smsCode.isPresent()) {
                    smsReceiversCustom.setSmsCode(smsCode.get());
                }

                smsReceiversCustom.setAppType(AppTypeConstant.TEACHER);
                Employee employee = employees1.get(0);
                smsReceiversCustom.setIdSchool(principal.getIdSchoolLogin());
                smsReceiversCustom.setNameUserReceiver(employee.getMaUser().getFullName());
                smsReceiversCustom.setNumberSms(totalSend);
                smsReceiversCustom.setPhoneUserReceiver(employee.getMaUser().getPhone());
            });
            smsReceiversCustom.setSendContent(contentSms);
            smsReceiversCustom.setSmsSendCustom(smsSendCustom);
            smsReceiversCustomRepository.save(smsReceiversCustom);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateStatusForEmployee(UserPrincipal principal, StatusCommonRequest request) {
        CommonValidate.checkDataPlus(principal);
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        EmployeeStatusTimeline employeeStatusLatest = employeeStatusTimelineRepository.findFirstByInfoEmployeeSchoolIdOrderByStartDateDesc(request.getId()).orElseThrow();
        if (!EmployeeConstant.STATUS_LEAVE.equals(infoEmployeeSchool.getEmployeeStatus())) {
            this.updateEmployeeStatus(infoEmployeeSchool, employeeStatusLatest, infoEmployeeSchool.getEmployeeStatus(), EmployeeConstant.STATUS_LEAVE, request.getDate());
            infoEmployeeSchool.setEmployeeStatus(EmployeeConstant.STATUS_LEAVE);
            infoEmployeeSchool.setDateRetain(null);
            infoEmployeeSchool.setDateLeave(request.getDate());
            infoEmployeeSchool.setActivated(AppConstant.APP_FALSE);
            infoEmployeeSchoolRepository.save(infoEmployeeSchool);
            this.clearApartmentAndClass(infoEmployeeSchool.getId(), EmployeeConstant.STATUS_LEAVE);
        }
        return true;
    }

    @Override
    public boolean updateEmployeeGroupOut(UserPrincipal principal, EmployeeGroupOutRequest request) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByIdInAndDelActiveTrue(request.getIdList());
        GroupOutEmployee groupOutEmployee = groupOutEmployeeRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdGroupOut(), principal.getIdSchoolLogin()).orElseThrow();
        LocalDate outDate = request.getDateOut();
        infoEmployeeSchoolList.forEach(x -> {
            if (!AppConstant.EMPLOYEE_STATUS_LEAVE.equals(x.getEmployeeStatus())) {
                this.updateOrCreateEmployeeStatusTimeline(x, EmployeeConstant.STATUS_LEAVE, outDate);
            }
            x.setDelActive(AppConstant.APP_FALSE);
            x.setEmployeeStatus(EmployeeConstant.STATUS_LEAVE);
            x.setOutDate(outDate);
            x.setDateLeave(outDate);
            x.setGroupOutEmployee(groupOutEmployee);
        });
        infoEmployeeSchoolRepository.saveAll(infoEmployeeSchoolList);
        return true;
    }

    @Override
    public ListEmployeeGroupOutResponse searchEmployeeGroupOut(UserPrincipal principal, SearchEmployeeGroupOutRequest request) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.searchGroupOut(principal.getIdSchoolLogin(), request);
        long total = infoEmployeeSchoolRepository.countGroupOut(principal.getIdSchoolLogin(), request);
        ListEmployeeGroupOutResponse response = new ListEmployeeGroupOutResponse();
        List<EmployeeGroupOutResponse> dataList = new ArrayList<>();
        for (InfoEmployeeSchool infoEmployeeSchool : infoEmployeeSchoolList) {
            EmployeeGroupOutResponse model = modelMapper.map(infoEmployeeSchool, EmployeeGroupOutResponse.class);
            dataList.add(model);
        }
        response.setDataList(dataList);
        response.setTotal(total);
        return response;
    }

    @Override
    public EmployeeDetailGroupOutResponse findByIdEmployeeGroupOut(UserPrincipal principal, Long id) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolId(id, principal.getIdSchoolLogin()).orElseThrow();
        EmployeeDetailGroupOutResponse response = modelMapper.map(infoEmployeeSchool, EmployeeDetailGroupOutResponse.class);
        List<AccountType> accountTypeList = infoEmployeeSchool.getAccountTypeList();
        response.setIdAccountTypeList(accountTypeList.stream().map(BaseEntity::getId).collect(Collectors.toList()));
        return response;
    }

    @Transactional
    @Override
    public boolean restoreEmployeeGroupOut(UserPrincipal principal, Long id) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolId(id, principal.getIdSchoolLogin()).orElseThrow();
        //cập nhật trạng thái đang làm cho nhân sự
        this.updateOrCreateEmployeeStatusTimeline(infoEmployeeSchool, EmployeeConstant.STATUS_WORKING, LocalDate.now());
        infoEmployeeSchool.setEmployeeStatus(EmployeeConstant.STATUS_WORKING);
        infoEmployeeSchool.setDelActive(AppConstant.APP_TRUE);
        infoEmployeeSchool.setOutDate(null);
        infoEmployeeSchool.setGroupOutEmployee(null);
        infoEmployeeSchool.setDateLeave(null);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
        return true;
    }

    private List<Employee> getEmployeeByPhone(String phone, List<Employee> employee) {
        List<Employee> employeeList = employee.stream().filter(x -> x.getMaUser().getPhone().equalsIgnoreCase(phone)).collect(Collectors.toList());
        return employeeList;
    }

    @Override
    @Transactional
    public boolean deleteEmployee(UserPrincipal principal, Long idInfoEmployee) {
//        CommonValidate.checkDataPlus(principal);
        if (principal.getAppType().equals(AppTypeConstant.SUPPER_SCHOOL) || principal.getAppType().equals(AppTypeConstant.SCHOOL)) {
            CommonUtil.checkDeleteObject(principal);
            this.deleteEmployeeCommon(idInfoEmployee, principal.getIdSchoolLogin());
        }
        return true;
    }

    @Override
    public boolean deleteEmployeeAdmin(Long id) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        infoEmployeeSchool.setDelActive(AppConstant.APP_FALSE);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
        return true;
    }

    @Override
    public boolean restoreEmployeeAdmin(Long id) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActive(id, AppConstant.APP_FALSE).orElseThrow();
        infoEmployeeSchool.setDelActive(AppConstant.APP_TRUE);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteMultiEmployee(UserPrincipal principal, Long[] ids) {
        CommonValidate.checkDataPlus(principal);
        CommonUtil.checkDeleteObject(principal);
        Long idSchool = principal.getIdSchoolLogin();
        for (Long idInfoEmployee : ids) {
            this.deleteEmployeeCommon(idInfoEmployee, idSchool);
        }
        return true;
    }


    @Override
    public ListExEmployeeClassResponse findByIdEmployeeClass(Long idEmployee) {
        /*List<ExEmployeeClass> exEmployeeClassList = exEmployeeClassRepository.findByEmployeeIdAndEmployeeEmployeeStatusNotAndDelActive(idEmployee, AppConstant.EMPLOYEE_STATUS_LEAVE, AppConstant.APP_TRUE);
        if (CollectionUtils.isEmpty(exEmployeeClassList)) {
            return null;
        }
        List<ExEmployeeClassDTO> exEmployeeClassDTOList = listMapper.mapList(exEmployeeClassList, ExEmployeeClassDTO.class);

        ListExEmployeeClassResponse listExEmployeeClassResponse = new ListExEmployeeClassResponse(exEmployeeClassDTOList);

        return listExEmployeeClassResponse;*/
        return null;
    }

    @Override
    @Transactional
    public boolean deleteEmployeeDepartment(Long idInfoEmployee) {
        List<ExDepartmentEmployee> exDepartmentEmployeeList = exDepartmentEmployeeRepository.findByInfoEmployeeSchoolIdAndInfoEmployeeSchoolEmployeeStatusNotAndDelActiveTrue(idInfoEmployee, AppConstant.EMPLOYEE_STATUS_LEAVE);
        if (!CollectionUtils.isEmpty(exDepartmentEmployeeList)) {
            return false;
        }
        for (ExDepartmentEmployee exDepartmentEmployee : exDepartmentEmployeeList) {
            exDepartmentEmployee.getDepartment().setDelActive(AppConstant.APP_FALSE);
            return true;
        }
        return false;
    }

    @Override
    public List<AccountTypeOtherResponse> findAllAccountTypeByIdSchool(Long idSchool) {
        List<AccountType> accountTypeList = accountTypeRepository.findByIdSchool(idSchool);
        List<AccountTypeOtherResponse> responseList = listMapper.mapList(accountTypeList, AccountTypeOtherResponse.class);
        return responseList;
    }

    @Override
    public List<TabDepartmentResponse> getTabDepartmentInEmployee(Long idSchool, Long idInfoEmployee) {
        List<Department> departmentList = departmentRepository.findBySchoolIdAndDelActiveTrue(idSchool);
//        if (CollectionUtils.isEmpty(departmentList)) {
//            return null;
//        }
        List<TabDepartmentResponse> tabDepartmentResponseList = new ArrayList<>();
        List<ExDepartmentEmployee> exDepartmentEmployeeList = exDepartmentEmployeeRepository.findByInfoEmployeeSchool_IdAndDelActiveTrue(idInfoEmployee);
        for (Department department : departmentList) {
            TabDepartmentResponse tabDepartmentResponse = new TabDepartmentResponse();
            exDepartmentEmployeeList.forEach(exDepartmentEmployee -> {
                if (exDepartmentEmployee.getDepartment().getId().equals(department.getId())) {
                    tabDepartmentResponse.setCheckDepartment(AppConstant.APP_TRUE);
                    tabDepartmentResponse.setPosition(exDepartmentEmployee.getPosition());
                }
            });
            tabDepartmentResponse.setDepartmentName(department.getDepartmentName());
            tabDepartmentResponse.setId(department.getId());
            tabDepartmentResponseList.add(tabDepartmentResponse);
        }
        return tabDepartmentResponseList;
    }

    @Override
    public List<TabProfessionalResponse> getTabProfessionalInEmployee(Long idSchool, Long idEmployee) {
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        List<TabProfessionalResponse> tabProfessionalResponseList = new ArrayList<>();
        List<ExEmployeeClass> exEmployeeClassList = exEmployeeClassRepository.findByInfoEmployeeSchool_IdAndDelActiveTrue(idEmployee);
        for (MaClass maClass : maClassList) {
            TabProfessionalResponse tabProfessionalResponse = new TabProfessionalResponse();
            exEmployeeClassList.forEach(exEmployeeClass -> {
                if (exEmployeeClass.getMaClass().getId().equals(maClass.getId())) {
                    tabProfessionalResponse.setCheckIsClass(AppConstant.APP_TRUE);
                    tabProfessionalResponse.setIsMaster(exEmployeeClass.isMaster());
                    List<SubjectInClassResponse> subjectInClassResponseList = new ArrayList<>();
                    //Lấy các môn có trong lớp mà nhân viên đó được phân

                    Set<Subject> subjectSet = exEmployeeClass.getSubjectSet();
                    tabProfessionalResponse.setListIdSubject(subjectSet.stream().map(BaseEntity::getId).collect(Collectors.toList()));
                }
            });
            tabProfessionalResponse.setClassName(maClass.getClassName());
            tabProfessionalResponse.setId(maClass.getId());
            tabProfessionalResponse.setSubjectResponseList(maClass.getSubjectList().stream().map(item -> modelMapper.map(item, SubjectInClassResponse.class)).collect(Collectors.toList()));
            tabProfessionalResponseList.add(tabProfessionalResponse);
        }
        return tabProfessionalResponseList;
    }

    @Override
    public List<SubjectInClassResponse> getSubjectInClass(Long idClass, Long idSchool) {
        List<Subject> subjectList = subjectRepository.findByMaClassListIdAndMaClassListDelActiveTrueAndDelActiveTrueAndIdSchool(idClass, idSchool);
        if (CollectionUtils.isEmpty(subjectList)) {
            return null;
        }

        List<SubjectInClassResponse> subjectInClassResponseList = new ArrayList<>();
        for (Subject subject : subjectList) {
            SubjectInClassResponse subjectInClassResponse = new SubjectInClassResponse();
            subjectInClassResponse.setSubjectName(subject.getSubjectName());
            subjectInClassResponse.setId(subject.getId());
            subjectInClassResponse.setIdClass(idClass);
            subjectInClassResponseList.add(subjectInClassResponse);
        }
        return subjectInClassResponseList;
    }

    /**
     * Hàm chuẩn hóa chuỗi
     *
     * @param str
     * @return
     */
    public String chuanHoa(String str) {
        str = str.trim();
        str = str.replaceAll("\\s+", " ");
        return str;
    }

    /**
     * Hàm chỉnh kích thước ảnh chiều rộng chiều cao
     *
     * @param inputImagePath
     * @param outputImagePath
     * @param scaledWidth
     * @param scaledHeight
     * @throws IOException
     */
    public void resize(String inputImagePath,
                       String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    /**
     * Tinh gia tri height moi sau khi resize
     *
     * @param pathFileName
     * @return
     * @throws IOException
     */
    public int calculateHeightNew(String pathFileName) throws IOException {
//        //converting file format
//        FileInputStream fis = new FileInputStream(new File(pathFileName));
//        BufferedImage bimg = ImageIO.read(fis);
        float height = 0f;
        float width = 0f;
        InputStream inputStream = new FileInputStream(pathFileName);
        try (ImageInputStream in = ImageIO.createImageInputStream(inputStream)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    height = reader.getHeight(0);
                    width = reader.getWidth(0);
                } finally {
                    reader.dispose();
                }
            }
        }
        float rate = height / width;
        float scaledHeightNew = (float) Math.ceil(rate * 512);
        return (int) scaledHeightNew;
    }

    /**
     * create account and employee
     *
     * @param infoEmployeeSchool
     */
    private void createAccountAndEmployee(InfoEmployeeSchool infoEmployeeSchool) {
        String fullName = infoEmployeeSchool.getFullName();
        String phone = infoEmployeeSchool.getPhone();
        String appType = infoEmployeeSchool.getAppType();
        if (StringUtils.isBlank(fullName) || StringUtils.isBlank(phone) || StringUtils.isBlank(appType) || infoEmployeeSchool == null) {
            logger.warn(ErrorsConstant.NO_DATA_ACCOUNT);
            return;
        }
        Long idSchool = infoEmployeeSchool.getSchool().getId();
        AccountCreateData accountCreateData = new AccountCreateData();
        String username = null;
        if (AppTypeConstant.SCHOOL.equals(appType)) {
            username = phone.concat(AppConstant.USERNAME_PLUS);
            accountCreateData.setAppType(AppTypeConstant.SCHOOL);
        } else if (AppTypeConstant.TEACHER.equals(appType)) {
            username = phone.concat(AppConstant.USERNAME_TEACHER);
            accountCreateData.setAppType(AppTypeConstant.TEACHER);
        }
        Optional<MaUser> maUserOptional = maUserRepository.findByUsername(username);
        if (maUserOptional.isPresent()) {
            logger.warn("username of " + appType + " đã tồn tại trong hệ thống");
            return;
        }
        //create account and role default
        accountCreateData.setFullName(fullName);
        accountCreateData.setUsername(username);
        accountCreateData.setPassword(GenerateCode.passwordAuto());
        accountCreateData.setPhone(phone);
        accountCreateData.setGender(infoEmployeeSchool.getGender());
        MaUser maUser = maUserService.createAccountOther(accountCreateData);

        //create employee
        Employee employee = new Employee();
        employee.setCode(GenerateCode.codeEmployee());
        employee.setIdSchoolLogin(idSchool);
        employee.setMaUser(maUser);
        employee.setEmail(infoEmployeeSchool.getEmail());
        employee.setBirthday(infoEmployeeSchool.getBirthday());
        Employee employeeSaved = employeeRepository.save(employee);
        //update infoemployeeschool
        infoEmployeeSchool.setEmployee(employeeSaved);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
        this.addRoleForEmployee(infoEmployeeSchool);
    }

    private void getCurrentTimeMilisInitial() {
        this.currentMilistime = System.currentTimeMillis();
    }

    /**
     * set idclassLogin
     *
     * @param tabProfessionalRequestList
     * @param idInfoEmployeeSchool
     */
    private void setCreateIdClassLogin(List<TabProfessionalRequest> tabProfessionalRequestList, Long idInfoEmployeeSchool) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findById(idInfoEmployeeSchool).orElseThrow(() -> new NotFoundException("not found by idInforEmployeeSchool"));
        if (infoEmployeeSchool.getEmployee() != null && !CollectionUtils.isEmpty(tabProfessionalRequestList)) {
            Long idClass = tabProfessionalRequestList.get(0).getId();
            Employee employee = infoEmployeeSchool.getEmployee();
            employee.setIdClassLogin(idClass);
            employeeRepository.save(employee);
        }
    }

    /**
     * cập nhật lại idClassLlogin
     *
     * @param tabProfessionalRequestList
     * @param idInfoEmployeeSchool
     */
    private void setUpdateIdClassLogin(List<TabProfessionalRequest> tabProfessionalRequestList, Long idInfoEmployeeSchool) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findById(idInfoEmployeeSchool).orElseThrow(() -> new NotFoundException("not found by idInforEmployeeSchool"));
        Employee employee = infoEmployeeSchool.getEmployee();
        if (employee != null) {
            if (CollectionUtils.isEmpty(tabProfessionalRequestList)) {
//                MaClass maClass = EmployeeUtil.getMaClassFromEmployee(employee);
//                if (maClass == null) {
                employee.setIdClassLogin(0L);
//                } else {
//                    employee.setIdClassLogin(maClass.getId());
//                    employee.setIdSchoolLogin(maClass.getIdSchool());
//                }
            } else {
                long count = tabProfessionalRequestList.stream().filter(x -> x.getId().equals(employee.getIdClassLogin())).count();
                if (count == 0) {
                    Long idClass = tabProfessionalRequestList.get(0).getId();
                    employee.setIdClassLogin(idClass);
                    MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow();
                    employee.setIdSchoolLogin(maClass.getIdSchool());
                }
            }
        }
        employeeRepository.save(employee);
    }

    private SmsDTO getSmsDTOByShoolId(long shoolId) {
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setCurrLocalDateTime(LocalDateTime.now());
        Optional<School> school = schoolRepository.findById(shoolId);
        if (school.isPresent()) {
            if (school.get().getBrand() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_BRAND);
            }
            Brand brand = school.get().getBrand();
            if (brand != null) {
                smsDTO.setBrandName(brand.getBrandName());
                if (brand.isBrandTypeAds()) {
                    smsDTO.setBrandType(0);
                } else {
                    smsDTO.setBrandType(1);
                }
                Supplier supplier = brand.getSupplier();
                if (supplier != null) {
                    smsDTO.setSupplierCode(supplier.getSupplierName());
                    smsDTO.setServiceUrl(supplier.getSupplierLink());
                    smsDTO.setUserName(supplier.getUsernameLink());
                    smsDTO.setPassowrd(supplier.getPasswordLink());
                }
            }
        }
        return smsDTO;
    }

    /**
     * //nếu chưa có tài khoản thì tạo tài khoản
     *
     * @param newInfoEmployeeSchool
     */
    private void createAccountNotExist(InfoEmployeeSchool newInfoEmployeeSchool) {
        //nếu chưa có tài khoản thì tạo tài khoản
        if (newInfoEmployeeSchool.getEmployee() == null) {
            this.createAccountAndEmployee(newInfoEmployeeSchool);
        }
    }

    private void setDateStatus(UpdateEmployeeMainInfoRequest updateEmployeeMainInfoRequest) {
        if (updateEmployeeMainInfoRequest.getEmployeeStatus().equals(EmployeeConstant.STATUS_WORKING)) {
            updateEmployeeMainInfoRequest.setDateRetain(null);
            updateEmployeeMainInfoRequest.setDateLeave(null);
        }
        if (updateEmployeeMainInfoRequest.getEmployeeStatus().equals(EmployeeConstant.STATUS_RETAIN)) {
            updateEmployeeMainInfoRequest.setDateLeave(null);
        }
        if (updateEmployeeMainInfoRequest.getEmployeeStatus().equals(EmployeeConstant.STATUS_LEAVE)) {
            updateEmployeeMainInfoRequest.setDateRetain(null);
        }
    }

    private LocalDate getDateWithEmployeeStatus(String status, LocalDate dateRetain, LocalDate dataLeave) {
        if (status.equals(EmployeeConstant.STATUS_WORKING)) {
            return LocalDate.now();
        }
        if (status.equals(EmployeeConstant.STATUS_RETAIN)) {
            return dateRetain;
        }
        if (status.equals(EmployeeConstant.STATUS_LEAVE)) {
            return dataLeave;
        }
        return null;
    }

    private void updateOrCreateEmployeeStatusTimeline(InfoEmployeeSchool oldInfoEmployeeSchool, String newStatus, LocalDate newDate) {
        if (newDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATE);
        }
        Long idInfoEmployee = oldInfoEmployeeSchool.getId();
        String oldStatus = oldInfoEmployeeSchool.getEmployeeStatus();
        //lấy ra lần thay đổi trạng thái mới nhất
        EmployeeStatusTimeline employeeStatusLatest = employeeStatusTimelineRepository.findFirstByInfoEmployeeSchoolIdOrderByStartDateDesc(idInfoEmployee).orElseThrow();
        //cùng trạng thái
        if (newStatus.equals(oldInfoEmployeeSchool.getEmployeeStatus())) {
            //trường hợp tạm nghỉ hoặc nghỉ làm
            if (newStatus.equals(EmployeeConstant.STATUS_RETAIN) || newStatus.equals(EmployeeConstant.STATUS_LEAVE)) {
                //cập nhật lại ngày tạm nghỉ hoặc nghỉ làm
                if (!employeeStatusLatest.getStartDate().isEqual(newDate)) {
                    //lấy ra lần thay đổi trạng thái trước đó
                    List<EmployeeStatusTimeline> kidsStatusTimelineList = employeeStatusTimelineRepository.findByInfoEmployeeSchoolIdOrderByStartDateDesc(idInfoEmployee);
                    EmployeeStatusTimeline statusTimelineBefore = kidsStatusTimelineList.get(1);
                    LocalDate startDateBefore = statusTimelineBefore.getStartDate();
                    if (newDate.isBefore(startDateBefore) || newDate.isEqual(startDateBefore)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày " + newStatus.toLowerCase() + " phải lớn ngày " + ConvertData.formartDateDash(startDateBefore));
                    }
                    //set lại 2 khoảng thời gian cho lần trước và lần hiện tại
                    statusTimelineBefore.setEndDate(newDate.minusDays(1));
                    employeeStatusLatest.setStartDate(newDate);
                    employeeStatusTimelineRepository.save(statusTimelineBefore);
                    employeeStatusTimelineRepository.save(employeeStatusLatest);
                }
            }

        } else {
            this.updateEmployeeStatus(oldInfoEmployeeSchool, employeeStatusLatest, oldStatus, newStatus, newDate);
            //khác trạng thái
//            boolean check = this.checkStartDateEqualNewDate(oldStatus, newStatus, employeeStatusLatest.getStartDate(), newDate);
////            cùng ngày
//            if (check) {
//                employeeStatusLatest.setStatus(newStatus);
//                employeeStatusTimelineRepository.save(employeeStatusLatest);
//            } else {
//                //lớn hơn ngày trước đó
//                //cập nhật lại ngày kết thúc cho lần gần nhất
//                employeeStatusLatest.setEndDate(newDate.minusDays(1));
//                //tạo lần mới
//                EmployeeStatusTimeline entity = new EmployeeStatusTimeline();
//                entity.setStartDate(newDate);
//                entity.setStatus(newStatus);
//                entity.setInfoEmployeeSchool(oldInfoEmployeeSchool);
//                employeeStatusTimelineRepository.save(entity);
//            }
        }
    }

    private void updateEmployeeStatus(InfoEmployeeSchool infoEmployeeSchool, EmployeeStatusTimeline employeeStatusLatest, String oldStatus, String newStatus, LocalDate newDate) {
        boolean check = this.checkStartDateEqualNewDate(oldStatus, newStatus, employeeStatusLatest.getStartDate(), newDate);
//            cùng ngày
        if (check) {
            employeeStatusLatest.setStatus(newStatus);
            employeeStatusTimelineRepository.save(employeeStatusLatest);
        } else {
            //lớn hơn ngày trước đó
            //cập nhật lại ngày kết thúc cho lần gần nhất
            employeeStatusLatest.setEndDate(newDate.minusDays(1));
            //tạo lần mới
            EmployeeStatusTimeline entity = new EmployeeStatusTimeline();
            entity.setStartDate(newDate);
            entity.setStatus(newStatus);
            entity.setInfoEmployeeSchool(infoEmployeeSchool);
            employeeStatusTimelineRepository.save(entity);
        }
    }

    /**
     * check ngày tạo trước đó và trạng thái thay đổi ngày có trùng nhau ko
     *
     * @param startDate
     * @param newDate
     * @return
     */
    private boolean checkStartDateEqualNewDate(String oldStatus, String newStatus, LocalDate startDate, LocalDate newDate) {
        if (newDate.isBefore(startDate)) {
            if (newStatus.equals(EmployeeConstant.STATUS_WORKING)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhân sự đang ở trạng thái " + oldStatus.toLowerCase() + " từ ngày " + ConvertData.formartDateDash(startDate) + ", vui lòng chọn lại từ ngày " + ConvertData.formartDateDash(startDate));
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày " + newStatus.toLowerCase() + " phải lớn ngày hoặc bằng ngày " + ConvertData.formartDateDash(startDate));
        }
        return newDate.isEqual(startDate);
    }

    private void deleteEmployeeCommon(Long idInfoEmployee, Long idSchool) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(idInfoEmployee, idSchool).orElseThrow();
        infoEmployeeSchool.setDelActive(AppConstant.APP_FALSE);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
    }

    private void clearApartmentAndClass(Long idInfoEmployee, String status) {
        if (StringUtils.equals(EmployeeConstant.STATUS_LEAVE, status)) {
            this.deleteEmployeeClass(idInfoEmployee);
            exDepartmentEmployeeRepository.deleteByInfoEmployeeSchoolId(idInfoEmployee);
        }
    }
}
