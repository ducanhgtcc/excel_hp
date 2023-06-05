package com.example.onekids_project.security.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.onecam.DeviceCam;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.school.SearchAccountRequest;
import com.example.onekids_project.master.response.ProfileMobileResponse;
import com.example.onekids_project.mobile.request.ProfileMobielRequest;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.mauser.*;
import com.example.onekids_project.request.user.AppTypeRequest;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.common.InforRepresentationResponse;
import com.example.onekids_project.response.device.DeviceLoginResponse;
import com.example.onekids_project.response.device.DeviceOneCamResponse;
import com.example.onekids_project.response.mauser.*;
import com.example.onekids_project.response.user.UserRoleResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.*;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.servicecustom.EmployeeService;
import com.example.onekids_project.service.servicecustom.KidsService;
import com.example.onekids_project.service.servicecustom.ParentService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import com.example.onekids_project.validate.RequestValidate;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaUserServiceImpl implements MaUserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private KidsService kidsService;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ParentService parentService;

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceCamRepository deviceCamRepository;


    @Override
    public void checkUserInMaUser(MaUser maUser) {
        if (!maUser.isDelActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_DELETE);
        }
        if (!maUser.isActivated()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_INACTIVATE);
        }
    }

    @Override
    public boolean checkUserInSchool(MaUser maUser) {
        String appType = maUser.getAppType();
        if (appType.equals(AppTypeConstant.SCHOOL)) {
            this.checkIdSchoolEmployeePlus(maUser.getEmployee(), AppTypeConstant.SCHOOL);
        } else if (appType.equals(AppTypeConstant.TEACHER)) {
            this.checkIdSchoolEmployeePlus(maUser.getEmployee(), AppTypeConstant.TEACHER);
        } else if (appType.equals(AppTypeConstant.PARENT)) {
            return this.checkParent(maUser.getParent());
        }
        return true;
    }

    private boolean checkParent(Parent parent) {
        List<Kids> kidsList = parent.getKidsList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(kidsList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_KID_EMPTY);
        }
        List<Kids> kidsLoginList = kidsList.stream().filter(x -> x.getId().equals(parent.getIdKidLogin())).collect(Collectors.toList());
        Kids kids;
        if (CollectionUtils.isEmpty(kidsLoginList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_FOUND_KIDS);
        }
        kids = kidsLoginList.get(0);
        this.checkSchool(kids.getIdSchool());
        if (!kids.isActivated()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_KID_UNACTIVE);
        }
        return true;
    }

    private void checkIdSchoolEmployeePlus(Employee employee, String appType) {
        Long idSchoolLogin = employee.getIdSchoolLogin();
        //danh sánh nhân sự tại trường đang đăng nhập
        List<InfoEmployeeSchool> infoEmployeeSchoolInSchoolLoginList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId().equals(idSchoolLogin)).collect(Collectors.toList());

        //không có tài khoản nào
        if (CollectionUtils.isEmpty(infoEmployeeSchoolInSchoolLoginList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_EMPTY);
        }
        this.checkSchool(idSchoolLogin);
        //kiểm tra thông tin tài khoản ứng với trường đó
        //có một tài khoản
        if (infoEmployeeSchoolInSchoolLoginList.size() == 1) {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolInSchoolLoginList.get(0);
            if (!infoEmployeeSchool.isDelActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_DELETE_SCHOOL);
            }
            if (!infoEmployeeSchool.isActivated()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_INACTIVATE_SCHOOL);
            }
        } else {
            //có nhiều tài khoản
            List<InfoEmployeeSchool> validInfoInSchoolList = infoEmployeeSchoolInSchoolLoginList.stream().filter(x -> x.isDelActive() && x.isActivated()).collect(Collectors.toList());
            //không có tài khoản nào hợp lệ
            if (CollectionUtils.isEmpty(validInfoInSchoolList)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_NOT_FOUND);
            } else if (validInfoInSchoolList.size() > 1) {
                //có nhiều hơn 1 tài khoản hợp lệ
                logger.warn(appType + ": " + ErrorsConstant.ACCOUNT_MANY_INSCHOOL);
            }
        }
    }

    private void checkSchool(Long idSchool) {
        School school = schoolRepository.findById(idSchool).orElseThrow();
        //kiểm tra thông tin trường
        if (!school.isDelActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trường '" + school.getSchoolName() + "' đã bị xóa");
        }
        if (!school.isSchoolActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trường '" + school.getSchoolName() + "' không được kích hoạt");
        }
    }

    @Override
    public boolean updatePassword(UserPrincipal principal, ChangePasswordRequest changePasswordRequest) {
        this.checkPasswordValid(changePasswordRequest);
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found mauser by id in change password"));
        boolean checkUpdate = passwordEncoder.matches(changePasswordRequest.getOldPassword(), maUser.getPasswordHash());
        if (!checkUpdate) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu cũ không đúng");
        }
        if (!changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu mới và xác nhận mật khẩu không khớp nhau");
        }
        maUser.setPasswordShow(changePasswordRequest.getPassword());
        maUser.setPasswordHash(passwordEncoder.encode(changePasswordRequest.getPassword()));
        maUserRepository.save(maUser);
        return true;
    }

    @Override
    public void updatePasswordMany(List<MaUser> maUserList, String newPassword) {
        RequestValidate.checkPassword(newPassword);
        String passwordHash = passwordEncoder.encode(newPassword);
        maUserList.stream().forEach(x -> {
            x.setPasswordShow(newPassword);
            x.setPasswordHash(passwordHash);
        });
        maUserRepository.saveAll(maUserList);
    }

//    @Override
//    public void createRoleDefault(Long idUser, Long idSchool, String appType) {
//        Role rootRole = roleRepository.findByIdSchoolAndDelActiveTrue(idSchool).orElseThrow(() -> new NotFoundException("not found root role by idSchool"));
//        Role typeRole = null;
//        if (appType.equals(AppTypeConstant.SCHOOL)) {
//            typeRole = roleRepository.findByIdSchoolAndTypeAndDelActiveTrue(idSchool, AppTypeConstant.SCHOOL).orElseThrow(() -> new NotFoundException("not found role by typeRole"));
//        } else if (appType.equals(AppTypeConstant.TEACHER)) {
//            typeRole = roleRepository.findByIdSchoolAndTypeAndDelActiveTrue(idSchool, AppTypeConstant.TEACHER).orElseThrow(() -> new NotFoundException("not found role by typeRole"));
//        } else if (appType.equals(AppTypeConstant.PARENT)) {
//            typeRole = roleRepository.findByIdSchoolAndTypeAndDelActiveTrue(idSchool, AppTypeConstant.PARENT).orElseThrow(() -> new NotFoundException("not found role by typeRole"));
//        }
//        maUserRepository.insertUserRole(idUser, rootRole.getId());
//        if (typeRole != null) {
//            maUserRepository.insertUserRole(idUser, typeRole.getId());
//        }
//    }

    @Override
    public MaUser createAccountOther(AccountCreateData accountCreateData) {
        RequestValidate.checkDataCreateAccount(accountCreateData);
        MaUser maUser = new MaUser();
        this.setPropertiesOther(maUser, accountCreateData);
        MaUser maUserSaved = maUserRepository.save(maUser);
        return maUserSaved;
    }

    @Override
    public MaUser createAccountSupperAdmin(AdminDataRequest adminDataRequest) {
        MaUser maUser = new MaUser();
        this.setPropertiesSupperAdmin(maUser, adminDataRequest);
        MaUser maUserSaved = maUserRepository.save(maUser);
        return maUserSaved;
    }


    @Override
    public boolean checkActiveUser(MaUserActiveRequest maUserActiveRequest) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(maUserActiveRequest.getId()).orElseThrow(() -> new NotFoundException("not found mauser by id"));
        maUser.setActivated(maUserActiveRequest.isActivated());
        maUserRepository.save(maUser);
        return true;
    }

    @Override
    public boolean checkActiveManyUser(List<MaUserActiveRequest> maUserActiveRequestList) {
        if (CollectionUtils.isEmpty(maUserActiveRequestList)) {
            return false;
        }
        maUserActiveRequestList.forEach(x -> {
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow(() -> new NotFoundException("not found mauser by id"));
            maUser.setActivated(x.isActivated());
            maUserRepository.save(maUser);
        });
        return true;
    }

    @Override
    public void checkExistUsernameAndAppType(String username, String appType) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(appType)) {
            throw new NotFoundException("username hoặc apptype là null");
        }
        boolean checkExist = maUserRepository.existsByUsernameAndAppTypeAndDelActiveTrue(username, appType);
        if (checkExist) {
            throw new UsernameNotFoundException("Đã tồn tại username và apptype tương ứng");
        }
    }

    @Override
    public List<UserRoleResponse> findMaUserRoleSchool(UserPrincipal principal, AppTypeRequest request) {
        CommonValidate.checkDataSupperPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<MaUser> maUserList = new ArrayList<>();
        if (AppTypeConstant.SCHOOL.equals(request.getType()) || AppTypeConstant.TEACHER.equals(request.getType())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findByAppTypeAndSchoolIdAndDelActiveTrue(request.getType(), idSchool);
            maUserList = infoEmployeeSchoolList.stream().filter(x -> x.getEmployee() != null).map(x -> x.getEmployee().getMaUser()).distinct().collect(Collectors.toList());
        } else if (AppTypeConstant.PARENT.equals(request.getType())) {
            List<Kids> kidsList = kidsRepository.findByIdSchoolAndDelActiveTrue(idSchool);
            maUserList = kidsList.stream().filter(x -> x.getParent() != null).map(x -> x.getParent().getMaUser()).distinct().collect(Collectors.toList());
        }
        maUserList.forEach(x -> x.setRoleList(x.getRoleList().stream().filter(y -> y.isDelActive() && y.getIdSchool().equals(idSchool)).collect(Collectors.toList())));
        return listMapper.mapList(maUserList, UserRoleResponse.class);
    }

    @Override
    public ProfileResponse findProfile(UserPrincipal principal) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        ProfileResponse model = new ProfileResponse();
        modelMapper.map(maUser, model);
        model.setUsername(ConvertData.getUsernameNoExtend(maUser.getUsername()));
        this.setPropertiesResponse(maUser, model);
        return model;
    }

    @Override
    public ProfileMobileResponse findProfileMobile(UserPrincipal principal) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        ProfileMobileResponse model = new ProfileMobileResponse();
        modelMapper.map(maUser, model);

        model.setUsername(ConvertData.getUsernameNoExtend(maUser.getUsername()));
        model.setPassword(this.getPasswordHide(maUser.getPasswordShow()));
        this.setPropertiesMobileResponse(maUser, model, principal.getIdSchoolLogin());
        return model;
    }


    @Transactional
    @Override
    public boolean updateProfile(UserPrincipal principal, ProfileRequest profileRequest) throws IOException {
        String appType = principal.getAppType();
        RequestValidate.checkPhone(profileRequest.getPhone());
        if (appType.equals(AppTypeConstant.PARENT) || appType.equals(AppTypeConstant.TEACHER) || appType.equals(AppTypeConstant.SCHOOL)) {

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_SUPPORT_ACCOUNT);
        }
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(profileRequest.getId()).orElseThrow();
        modelMapper.map(profileRequest, maUser);
        this.setPropertiesForRequest(maUser, profileRequest);
        maUserRepository.save(maUser);
        return true;
    }

    @Transactional
    @Override
    public boolean updateProfileMobile(UserPrincipal principal, ProfileMobielRequest request) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        RequestValidate.checkPhone(request.getPhone());
        String appType = maUser.getAppType();
        modelMapper.map(request, maUser);
        if (appType.equals(AppTypeConstant.PARENT)) {
            this.updateProfileParent(request, maUser.getParent());
        } else if (appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.TEACHER)) {
            this.updateProfileEmployee(maUser, request.getBirthday(), request.getEmail());
        }
        maUserRepository.save(maUser);
        return true;
    }

    @Override
    public boolean updateAvatarMobile(UserPrincipal principal, MultipartFile multipartFile) throws IOException {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        String appType = maUser.getAppType();
        if (multipartFile == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_PICTURE);
        }
        if (appType.equals(AppTypeConstant.PARENT)) {
            this.updateAvatarParent(principal, multipartFile, maUser.getParent());
        } else if (appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.TEACHER)) {
            this.updateAvatarEmployee(maUser, multipartFile);
        }
        return true;
    }


    @Override
    public ListAccountResponse findAccount(SearchAccountRequest request) {
        ListAccountResponse response = new ListAccountResponse();
        List<MaUser> maUserList = maUserRepository.findAccount(request);
        long total = maUserRepository.countTotalAccount(request);
        List<AccountResponse> dataList = new ArrayList<>();
        maUserList.forEach(x -> {
            AccountResponse model = modelMapper.map(x, AccountResponse.class);
            model.setExtendUsername(ConvertData.getExtendUsernameString(model.getUsername()));
            model.setUsername(ConvertData.getUsernameNoExtend(model.getUsername()));
            List<Device> deviceList = deviceRepository.findByMaUserIdAndTypeInAndLoginTrueAndDelActiveTrue(x.getId(), Arrays.asList(DeviceTypeConstant.ANDROID, DeviceTypeConstant.IOS));
            model.setDeviceLoginList(listMapper.mapList(deviceList, DeviceLoginResponse.class));
            List<DeviceCam> camList = deviceCamRepository.findByMaUserIdAndLoginTrueAndForceLogoutFalse(x.getId());
            model.setDeviceCamList(listMapper.mapList(camList, DeviceOneCamResponse.class));
            dataList.add(model);
        });
        response.setDataList(dataList);
        response.setTotal(total);
        return response;
    }

    @Override
    public boolean updateAccount(AccountRequest request) {
        RequestValidate.checkUsernameNoExtend(request.getUsername());
        RequestValidate.checkPassword(request.getPasswordShow());
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        String extenUserName = ConvertData.getExtendUsername(maUser.getUsername());
        String newUsername = request.getUsername().concat(extenUserName);
        if (!maUser.getUsername().equals(newUsername)) {
            this.checkExistUsername(newUsername);
        }
        if (!maUser.getPasswordShow().equals(request.getPasswordShow())) {
            maUser.setPasswordHash(passwordEncoder.encode(request.getPasswordShow()));
        }
        modelMapper.map(request, maUser);
        maUser.setUsername(newUsername);
        maUserRepository.save(maUser);
        return true;
    }

    @Override
    public boolean deleteAccount(Long id) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        maUser.setTimeDelete(LocalDateTime.now());
        maUser.setDelActive(AppConstant.APP_FALSE);
        maUserRepository.save(maUser);
        return true;
    }

    @Override
    public void deleteCompleteAccount(Long id, String typeDelete) {
        MaUser maUser = maUserRepository.findById(id).orElseThrow();
        List<MaUser> maUserList = maUserRepository.findAccountHasExtendUsername(maUser.getUsername());
        String newExtend = ConvertData.getExtendNumberNextUsername(maUserList);
        String newUsernameExtendNumber = ConvertData.getNewUsernameAndConcatNumber(maUser.getUsername(), newExtend);
        maUser.setUsername(newUsernameExtendNumber);
        maUser.setTimeDeleteComplete(LocalDateTime.now());
        maUser.setTypeDelete(typeDelete);
        maUser.setDelActive(AppConstant.APP_FALSE);
        maUserRepository.save(maUser);
    }

    @Override
    public boolean restoreAccount(Long id) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveFalse(id).orElseThrow();
        String username = maUser.getUsername();
        String extendNumber = ConvertData.getExtendUsernameString(username);
        if (StringUtils.isNotBlank(extendNumber)) {
            String usernameNoNumber = ConvertData.getUsernameNoExtendNumber(username);
            Optional<MaUser> maUserOptional = maUserRepository.findByUsername(usernameNoNumber);
            if (maUserOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản " + maUser.getAppType() + " có username là '" + ConvertData.getUsernameNoExtend(username) + "' đã tồn tại");
            }
            maUser.setUsername(usernameNoNumber);
        }
        maUser.setStartDateDelete(null);
        maUser.setDelActive(AppConstant.APP_TRUE);
        maUserRepository.save(maUser);
        return true;
    }

    @Override
    public void checkExistUsername(String username) {
        Optional<MaUser> maUserOptional = maUserRepository.findByUsername(username);
        if (maUserOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_EXIST);
        }
    }

    @Override
    public void checkUsernameAndAppType(String usernameNoExtend, String appType) {
        if (StringUtils.isBlank(usernameNoExtend) || StringUtils.isBlank(appType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATA);
        }
        String username = ConvertData.getUsernameIncludeExtend(usernameNoExtend, appType);
        Optional<MaUser> maUserOptional = maUserRepository.findByUsername(username);
        if (maUserOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_EXIST);
        }
    }

    @Override
    public void checkUsername(String username, String appType) {
        RequestValidate.checkUsernameNoExtend(username);
    }

    @Override
    public void checkAccountHandle(UsernameRequest request) {
        RequestValidate.checkPhone(request.getUsername());
        this.checkUsernameAndAppType(request.getUsername(), request.getAppType());
    }

    @Transactional
    @Override
    public boolean createAccountWithNewPhone(HandleNewPhoneRequest request) {
        if (request.getAppType().equals(AppTypeConstant.SCHOOL) || request.getAppType().equals(AppTypeConstant.TEACHER)) {
            this.createAccountTeacherOrPlus(request);
        } else if (request.getAppType().equals(AppTypeConstant.PARENT)) {
            this.createAccountParent(request);
        }
        return true;
    }


    @Override
    public boolean verifycation(UserPrincipal principal, VerificationAccountRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        String verifyInSchool = school.getSchoolConfig().getVerifyCode();
        if (verifyInSchool.equals(request.getCode())) {
            return true;
        } else if (request.getAppType().equals(AppTypeConstant.SCHOOL) || request.getAppType().equals(AppTypeConstant.TEACHER)) {
            this.checkVerifyCodeTeacherOrPlus(request);
        } else if (request.getAppType().equals(AppTypeConstant.PARENT)) {
            this.checkVerifyCodeParent(request);
        }
        return true;
    }


    @Transactional
    @Override
    public boolean mergeAccount(MergeAccountRequest request) {
        if (request.getAppType().equals(AppTypeConstant.SCHOOL) || request.getAppType().equals(AppTypeConstant.TEACHER)) {
            this.mergeAccountTeacherOrPlus(request);
        } else if (request.getAppType().equals(AppTypeConstant.PARENT)) {
            this.mergeAccountParent(request);
        }
        return true;
    }

    @Override
    public List<AccountEmployeeHandleResponse> getAccountEmployeeDuplicate(UserPrincipal principal, AccountInforRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        RequestValidate.checkPhone(request.getPhone());
        String usernameAndExtend = ConvertData.getUsernameIncludeExtend(request.getPhone(), request.getAppType());
        Long idSchool = principal.getIdSchoolLogin();
        MaUser maUser = maUserRepository.findByUsername(usernameAndExtend).orElseThrow();
        List<AccountEmployeeHandleResponse> responseList = new ArrayList<>();
        List<InfoEmployeeSchool> infoEmployeeSchoolExistAccountList = maUser.getEmployee().getInfoEmployeeSchoolList().stream().filter(x -> x.isDelActive() && x.getSchool().getId().equals(idSchool)).collect(Collectors.toList());
        List<InfoEmployeeSchool> infoEmployeeWithPhoneAccountList = infoEmployeeSchoolRepository.findBySchoolIdAndPhoneAndAppTypeAndDelActiveTrue(idSchool, request.getPhone(), request.getAppType());
        List<InfoEmployeeSchool> infoEmployeeSchoolList = (List<InfoEmployeeSchool>) org.apache.commons.collections4.CollectionUtils.union(infoEmployeeSchoolExistAccountList, infoEmployeeWithPhoneAccountList);
        infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(FilterDataUtils.distinctBy(InfoEmployeeSchool::getId)).collect(Collectors.toList());
        List<AccountEmployeeHandleResponse> finalResponseList = responseList;
        infoEmployeeSchoolList.forEach(x -> {
            AccountEmployeeHandleResponse model = modelMapper.map(x, AccountEmployeeHandleResponse.class);
            if (x.getEmployee() != null) {
                model.setEixstAccount(AppConstant.APP_TRUE);
                model.setStatus(AppConstant.APP_TRUE);
                model.setUsername(ConvertData.getUsernameNoExtend(x.getEmployee().getMaUser().getUsername()));
            }
            finalResponseList.add(model);
        });
        responseList = responseList.stream().sorted(Comparator.comparing(AccountEmployeeHandleResponse::getCreatedDate).reversed()).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public List<AccountKidsHandleResponse> getAccountKidsDuplicate(UserPrincipal principal, String phone) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        RequestValidate.checkPhone(phone);
        Long idSchool = principal.getIdSchoolLogin();
        String usernameAndExtend = ConvertData.getUsernameIncludeExtend(phone, AppTypeConstant.PARENT);
        MaUser maUser = maUserRepository.findByUsername(usernameAndExtend).orElseThrow();
        List<Kids> kidsExistAccountWithPhoneList = maUser.getParent().getKidsList().stream().filter(x -> x.isDelActive() && x.getIdSchool().equals(idSchool)).collect(Collectors.toList());
        List<Kids> kidsWithPhoneRepresentationList = kidsRepository.findKidsWithPhoneRepresentationIndSchool(idSchool, phone);
        List<Kids> kidsList = (List<Kids>) org.apache.commons.collections4.CollectionUtils.union(kidsExistAccountWithPhoneList, kidsWithPhoneRepresentationList);
        kidsList = kidsList.stream().filter(FilterDataUtils.distinctBy(Kids::getId)).collect(Collectors.toList());
        List<AccountKidsHandleResponse> responseList = new ArrayList<>();
        List<AccountKidsHandleResponse> finalResponseList = responseList;
        kidsList.forEach(x -> {
            AccountKidsHandleResponse model = modelMapper.map(x, AccountKidsHandleResponse.class);
            if (x.getParent() != null) {
                model.setEixstAccount(AppConstant.APP_TRUE);
                model.setStatus(AppConstant.APP_TRUE);
                model.setUsername(ConvertData.getUsernameNoExtend(x.getParent().getMaUser().getUsername()));
            }
            model.setKidStatus(StudentUtil.getConvertKidStatus(x.getKidStatus()));
            InforRepresentationResponse inforRepresentationResponse = ConvertData.getInforRepresent(x);
            model.setPhone(inforRepresentationResponse.getPhone());
            finalResponseList.add(model);
        });
        responseList = responseList.stream().sorted(Comparator.comparing(AccountKidsHandleResponse::getCreatedDate).reversed()).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public MaUserParentResponse getMauserByUsername(String username, String appType) {
        String usernameAndExtend = ConvertData.getUsernameIncludeExtend(username, appType);
        MaUser maUser = maUserRepository.findByUsername(usernameAndExtend).orElseThrow();
        MaUserParentResponse response = new MaUserParentResponse();
        modelMapper.map(maUser, response);
        List<Kids> kidsList = maUser.getParent().getKidsList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        List<KidsInforMauserResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsInforMauserResponse model = modelMapper.map(x, KidsInforMauserResponse.class);
            model.setSchoolName(SchoolUtils.getSchoolName(x.getIdSchool()));
            responseList.add(model);
        });
        response.setKidList(responseList);
        return response;
    }


    /**
     * sử dụng tài khoản cũ
     *
     * @param maUser
     * @param kids
     */
    private void useOldAccountParent(MaUser maUser, Kids kids) {
        kids.setParent(maUser.getParent());
        kidsRepository.save(kids);
    }

    /**
     * sử dụng tài khoản cũ teacher or plus
     *
     * @param maUser
     * @param infoEmployeeSchool
     */
    private void useOldAccountTeacherOrPlus(MaUser maUser, InfoEmployeeSchool infoEmployeeSchool) {
        infoEmployeeSchool.setEmployee(maUser.getEmployee());
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
    }


    private void setPropertiesResponse(MaUser maUser, ProfileResponse model) {
        String appType = maUser.getAppType();
        if (appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.TEACHER)) {
            InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(maUser);
            model.setAvatar(infoEmployeeSchool.getAvatar());
            model.setEmail(infoEmployeeSchool.getEmail());
            model.setBirthday(infoEmployeeSchool.getBirthday());
        } else if (appType.equals(AppTypeConstant.PARENT)) {
            Parent dataDB = maUser.getParent();
            model.setAvatar(dataDB.getAvatar());
            model.setEmail(dataDB.getEmail());
            model.setBirthday(dataDB.getBirthday());
        }
    }

    private void setPropertiesMobileResponse(MaUser maUser, ProfileMobileResponse model, Long idSchool) {
        String appType = maUser.getAppType();
        model.setAvatar(ConvertData.getAvatarUserSchool(maUser));
        if (appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.TEACHER)) {
            InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(maUser);
            model.setEmail(infoEmployeeSchool.getEmail());
            model.setBirthday(ConvertData.formartDateDash(infoEmployeeSchool.getBirthday()));
        } else if (appType.equals(AppTypeConstant.PARENT)) {
            Parent dataDB = maUser.getParent();
            model.setEmail(dataDB.getEmail());
            model.setBirthday(dataDB.getBirthday() != null ? ConvertData.formartDateDash(dataDB.getBirthday()) : null);
        }
    }

    private void setPropertiesForRequest(MaUser maUser, ProfileRequest request) throws IOException {
        String appType = maUser.getAppType();
        if (appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.TEACHER)) {
            InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(maUser);
            if (request.getMultipartFile() != null) {
                HandleFileUtils.deletePictureInFolder(infoEmployeeSchool.getUrlLocalAvatar());
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(request.getMultipartFile(), SystemConstant.ID_SYSTEM, UploadDownloadConstant.AVATAR);
                infoEmployeeSchool.setAvatar(handleFileResponse.getUrlWeb());
                infoEmployeeSchool.setUrlLocalAvatar(handleFileResponse.getUrlLocal());
            }
            infoEmployeeSchool.setEmail(request.getEmail());
            infoEmployeeSchool.setBirthday(request.getBirthday());
        } else if (appType.equals(AppTypeConstant.PARENT)) {
            Parent dataDB = maUser.getParent();
            if (request.getMultipartFile() != null) {
                HandleFileUtils.deletePictureInFolder(dataDB.getAvatarLocal());
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSavedNoTime(request.getMultipartFile(), SystemConstant.ID_SYSTEM, UploadDownloadConstant.AVATAR);
                dataDB.setAvatar(handleFileResponse.getUrlWeb());
                dataDB.setAvatarLocal(handleFileResponse.getUrlLocal());
            }
            dataDB.setEmail(request.getEmail());
            dataDB.setBirthday(request.getBirthday());
        }
    }

    /**
     * kiểm tra mật khẩu có hợp lệ hay không
     *
     * @param model
     * @return
     */
    private void checkPasswordValid(ChangePasswordRequest model) {
        RequestValidate.checkPassword(model.getOldPassword());
        RequestValidate.checkPassword(model.getPassword());
        RequestValidate.checkPassword(model.getConfirmPassword());
    }

    private void setProperties(MaUser maUser, SignUpRequest signUpRequest) {
        maUser.setUsername(signUpRequest.getUsername());
        maUser.setPasswordShow(signUpRequest.getPassword());
        maUser.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));
        maUser.setAppType(signUpRequest.getAppType());
        maUser.setFullName(signUpRequest.getFullName());
        this.checkExistUsernameAndAppType(maUser.getUsername(), maUser.getAppType());
    }

    /**
     * set properties cho các TH tạo tài khoản ngoài giao diện đăng ký(hiện tại chưa có giao diện đăng ký)
     *
     * @param maUser
     * @param accountCreateData
     */
    private void setPropertiesOther(MaUser maUser, AccountCreateData accountCreateData) {
        String fullName = accountCreateData.getFullName();
        String username = accountCreateData.getUsername();
        String password = accountCreateData.getPassword();
        String appType = accountCreateData.getAppType();
        String phone = accountCreateData.getPhone();
        if (StringUtils.isBlank(fullName) || StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(appType) || StringUtils.isBlank(phone)) {
            throw new NotFoundException("Thông tin đăng nhập null");
        }
        boolean checkUsername = maUserRepository.existsByUsernameAndDelActiveTrue(username.trim());
        if (checkUsername) {
            throw new UsernameNotFoundException("Đã tồn tại username trong hệ thống");
        }
        maUser.setFullName(fullName.trim());
        maUser.setUsername(username.trim());
        maUser.setPasswordShow(password.trim());
        maUser.setPasswordHash(passwordEncoder.encode(password.trim()));
        maUser.setAppType(appType.trim());
        maUser.setPhone(phone.trim());
        maUser.setGender(accountCreateData.getGender());
    }

    private void setPropertiesSupperAdmin(MaUser maUser, AdminDataRequest adminDataRequest) {
        boolean checkUsername = maUserRepository.existsByUsernameAndDelActiveTrue(adminDataRequest.getUsername().trim());
        if (checkUsername) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đã tồn tại username trong hệ thống khi tạo supperAdmin");
        }
        maUser.setFullName(adminDataRequest.getFullName());
        maUser.setUsername(adminDataRequest.getUsername().trim());
        maUser.setPasswordShow(adminDataRequest.getPassword().trim());
        maUser.setPasswordHash(passwordEncoder.encode(adminDataRequest.getPassword().trim()));
        maUser.setAppType(AppTypeConstant.SUPPER_SYSTEM);
        maUser.setPhone("phone admin");
        maUser.setGender(AppConstant.MALE);
        maUser.setIdCreated(SystemConstant.ID_SYSTEM);
        maUser.setCreatedDate(LocalDateTime.now());
    }

    private void createAccountParent(HandleNewPhoneRequest request) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        InforRepresentationResponse model = ConvertData.getInforRepresent(kids);
        kidsService.createAccountAndParentForOther(model.getFullName(), request.getNewPhone(), kids);
    }

    private void createAccountTeacherOrPlus(HandleNewPhoneRequest request) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        infoEmployeeSchool.setPhone(request.getNewPhone());
        employeeService.createAccountAndEmployeeForOther(infoEmployeeSchool);
    }


    private void checkVerifyCodeParent(VerificationAccountRequest request) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        RequestValidate.checkVerifyCode(request.getCode());
        if (kids.getVerifyCodeAdmin().equals(request.getCode()) || kids.getVerifyCodeSchool().equals(request.getCode())) {
            return;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CODE_WRONG);
        }
    }

    private void checkVerifyCodeTeacherOrPlus(VerificationAccountRequest request) {
        InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        RequestValidate.checkVerifyCode(request.getCode());
        if (infoEmployeeSchool.getVerifyCodeAdmin().equals(request.getCode()) || infoEmployeeSchool.getVerifyCodeSchool().equals(request.getCode())) {
            return;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CODE_WRONG);
        }
    }

    private void mergeAccountParent(MergeAccountRequest request) {
        String usernameAndExtend = ConvertData.getUsernameIncludeExtend(request.getPhone(), request.getAppType());
        MaUser maUser = maUserRepository.findByUsername(usernameAndExtend).orElseThrow();
        if (request.getType().equals(AppConstant.OLD_ACCOUNT)) {
            List<AccountAndStatusRequest> accountAndStatusRequestList = request.getUserList();
            this.checkDataKidsOldAccount(accountAndStatusRequestList);
            //vòng lặp kiểm tra các tài khoản
            accountAndStatusRequestList.forEach(x -> {
                Kids kids = kidsRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
                //được chọn
                if (x.isStatus()) {
                    //chưa có tài khoản parent thì nối tài khoản con này đến tài khoản parent
                    if (kids.getParent() == null) {
                        this.useOldAccountParent(maUser, kids);
                    }
                    Parent parent = kids.getParent();
                    parent.setIdKidLogin(kids.getId());
                    //tạo ví cho phụ huynh nếu con được chọn trong trường mà phụ huynh chưa có ví cho trường đó
                    parentService.createWalletParent(parent, kids.getIdSchool());
                    maUser.setDelActive(AppConstant.APP_TRUE);
                    maUserRepository.save(maUser);
                } else {
                    //không được chọn
                    kids.setDelActive(AppConstant.APP_FALSE);
                    kidsRepository.save(kids);
                }
            });
        } else if (request.getType().equals(AppConstant.NEW_ACCOUNT)) {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
            this.deleteCompleteAccount(maUser.getId(), AccountTypeConstant.DELETE_HANDLE);
            InforRepresentationResponse model = ConvertData.getInforRepresent(kids);
            kidsService.createAccountAndParentForOther(model.getFullName(), request.getPhone(), kids);
        }
    }

    private void mergeAccountTeacherOrPlus(MergeAccountRequest request) {
        String usernameAndExtend = ConvertData.getUsernameIncludeExtend(request.getPhone(), request.getAppType());
        MaUser maUser = maUserRepository.findByUsername(usernameAndExtend).orElseThrow();
        if (request.getType().equals(AppConstant.OLD_ACCOUNT)) {
            List<AccountAndStatusRequest> accountAndStatusRequestList = request.getUserList();
            this.checkDataEmployeeOldAccount(accountAndStatusRequestList);
            accountAndStatusRequestList.forEach(x -> {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
                //một tài khoản được chọn
                if (x.isStatus()) {
                    //chưa có tài khoản thì set idEmployee ứng với tài khoản cho người dùng này có sdt này
                    if (infoEmployeeSchool.getEmployee() == null) {
                        this.useOldAccountTeacherOrPlus(maUser, infoEmployeeSchool);
                    }
                    maUser.setDelActive(AppConstant.APP_TRUE);
                    maUserRepository.save(maUser);
                } else {
                    //các tài khoản khác set trạng thái delActive=false
                    infoEmployeeSchool.setDelActive(AppConstant.APP_FALSE);
                    infoEmployeeSchoolRepository.save(infoEmployeeSchool);
                }
            });
        } else if (request.getType().equals(AppConstant.NEW_ACCOUNT)) {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
            this.deleteCompleteAccount(maUser.getId(), AccountTypeConstant.DELETE_HANDLE);
            employeeService.createAccountAndEmployeeForOther(infoEmployeeSchool);
        }
    }

    private void checkDataEmployeeOldAccount(List<AccountAndStatusRequest> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATA);
        }
        long count = dataList.stream().filter(AccountAndStatusRequest::isStatus).count();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.NO_ACCOUNT);
        } else if (count > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.ACCOUNT_UNIQUE);
        }
    }

    private void checkDataKidsOldAccount(List<AccountAndStatusRequest> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATA);
        }
        long count = dataList.stream().filter(AccountAndStatusRequest::isStatus).count();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.NO_ACCOUNT);
        }
    }

    private String getPasswordHide(String password) {
        String text = "";
        while (text.length() < password.length()) {
            text = text.concat("*");
        }
        return text;
    }

    private void updateAvatarParent(UserPrincipal principal, MultipartFile multipartFile, Parent parent) throws IOException {
        if (StringUtils.isNotBlank(parent.getAvatar())) {
            HandleFileUtils.deletePictureInFolder(parent.getAvatarLocal());
        }
        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(multipartFile, principal.getIdSchoolLogin(), UploadDownloadConstant.AVATAR);
        parent.setAvatar(handleFileResponse.getUrlWeb());
        parent.setAvatarLocal(handleFileResponse.getUrlLocal());
        parentRepository.save(parent);
    }

    private void updateAvatarEmployee(MaUser maUser, MultipartFile multipartFile) throws IOException {
        InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(maUser);
        if (StringUtils.isNotBlank(infoEmployeeSchool.getAvatar())) {
            HandleFileUtils.deletePictureInFolder(infoEmployeeSchool.getUrlLocalAvatar());
        }
        HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(multipartFile, SchoolUtils.getIdSchool(), UploadDownloadConstant.AVATAR);
        infoEmployeeSchool.setAvatar(handleFileResponse.getUrlWeb());
        infoEmployeeSchool.setUrlLocalAvatar(handleFileResponse.getUrlLocal());
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
    }

    private void updateProfileParent(ProfileMobielRequest request, Parent parent) {
        parent.setBirthday(request.getBirthday());
        parent.setEmail(request.getEmail());
        parentRepository.save(parent);
    }

    private void updateProfileEmployee(MaUser maUser, LocalDate birthday, String email) {
        if (birthday == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.BIRTHDAY_EMPTY);
        }
        InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(maUser);
        infoEmployeeSchool.setBirthday(birthday);
        infoEmployeeSchool.setEmail(email);
        infoEmployeeSchoolRepository.save(infoEmployeeSchool);
    }
}
