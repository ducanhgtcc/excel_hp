package com.example.onekids_project.security.controller;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mobile.service.servicecustom.DeviceCamService;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.jwt.JwtTokenProvider;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.*;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.servicecustom.DeviceService;
import com.example.onekids_project.util.ChangeUsernameUtil;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.PermissionUtils;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.RequestValidate;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * controller thực hiện đăng ký, đăng nhập
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MaUserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MaUserService maUserService;

    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceCamService deviceCamService;


    /**
     * @param loginRequest thông tin đăng nhập
     * @return thông tin đăng nhập thành công
     */
    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        loginRequest = this.getLoginWebRequest(loginRequest);
        logger.info("login information web: {} ", loginRequest);
        String usernameExtend = ChangeUsernameUtil.changeUsername(loginRequest.getAppType());
        loginRequest.setUsername(loginRequest.getUsername().concat(usernameExtend));
        //kiểm tra usernamne và password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        // lấy ra id người dùng đang đăng nhập
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long idSchool = userPrincipal.getIdSchoolLogin();
        Long userId = userPrincipal.getId();
        MaUser maUser = maUserRepository.findById(userId).orElseThrow();
        //kiểm tra tính hợp lệ của tài khoản
        maUserService.checkUserInMaUser(maUser);

        //kiểm tra thông tin tài khoản trong 1 trường
        maUserService.checkUserInSchool(maUser);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // các giá trị trả về khi đăng nhập thành công
        String appType = maUser.getAppType();
        String currentUser = maUser.getFullName();
        Set<String> apiSet = PermissionUtils.getApiOfUser(maUser, idSchool);

        response.setAccessToken(jwt);
        response.setCurrentUser(currentUser);
        response.setAppType(appType);
        response.setApiSet(apiSet);
        this.setAvatarUser(maUser, response);
        this.setInforSchool(userPrincipal, maUser, response);
        logger.info("------Login success web------");
        return NewDataResponse.setDataCustom(response, MessageWebConstant.LOGIN_SUCCESS);
    }


    /**
     * đăng nhập dành cho mobile
     *
     * @param loginMobileRequest
     * @return
     */
    @PostMapping("/mob/signin")
    public ResponseEntity authenticateUserMobile(@Valid @RequestBody LoginMobileRequest loginMobileRequest) {
        loginMobileRequest = this.getLoginRequest(loginMobileRequest);
        logger.info("login information mobile: {} ", loginMobileRequest);
        LoginRequest checkLoginRequest = modelMapper.map(loginMobileRequest, LoginRequest.class);
        String usernameExtend = ChangeUsernameUtil.changeUsername(loginMobileRequest.getAppType());
        loginMobileRequest.setUsername(loginMobileRequest.getUsername().concat(usernameExtend));

        RequestValidate.loginRequestValidate(checkLoginRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginMobileRequest.getUsername(),
                        loginMobileRequest.getPassword()
                )
        );
        // lấy ra id người dùng đang đăng nhập
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        if (loginMobileRequest.isCamera()) {
            deviceCamService.checkDeviceCamLimit(userPrincipal);
            deviceCamService.saveDeviceCame(userId, loginMobileRequest.getIdDevice(), loginMobileRequest.getDeviceType());
        } else {
            deviceService.checkDeviceLimit(userPrincipal, loginMobileRequest.getIdDevice());
        }
        MaUser maUser = maUserRepository.findById(userId).orElseThrow();
        //kiểm tra tính hợp lệ của tài khoản
        maUserService.checkUserInMaUser(maUser);
        //kiểm tra thông tin tài khoản trong 1 trường
        maUserService.checkUserInSchool(maUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();

        String appType = maUser.getAppType();
        //tên người đăng nhập
        String currentUser = maUser.getFullName();

        //login for parent
        if (AppTypeConstant.PARENT.equals(appType)) {
            //login success
            LoginSuccessResponse loginSuccessResponse = new LoginSuccessResponse();
            loginSuccessResponse.setToken(jwt);
            String avatar = maUser.getParent().getAvatar();
            if (StringUtils.isBlank(avatar)) {
                avatar = AvatarDefaultConstant.AVATAR_PARENT;
            }
            loginSuccessResponse.setAvatar(avatar);
            logger.info("------Login success mobile parent------");
            return DataResponse.getData(loginSuccessResponse, HttpStatus.OK);
        } else if (AppTypeConstant.TEACHER.equals(appType)) {
            //login for teacher
            LoginSuccessTeacherResponse loginSuccessResponse = new LoginSuccessTeacherResponse();
            loginSuccessResponse.setId(maUser.getId());
            loginSuccessResponse.setToken(jwt);
            loginSuccessResponse.setAvatar(StringUtils.isNotBlank(maUser.getEmployee().getAvatar()) ? maUser.getEmployee().getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER);
            logger.info("------Login success mobile teacher------");
            return DataResponse.getData(loginSuccessResponse, HttpStatus.OK);
        } else if (AppTypeConstant.SCHOOL.equals(appType)) {
            //login for plus
            LoginSuccessPlusResponse loginSuccessResponse = new LoginSuccessPlusResponse();
            loginSuccessResponse.setToken(jwt);
            loginSuccessResponse.setAvatar(ConvertData.getAvatarUserSchool(maUser));
            logger.info("------Login success mobile plus------");
            return NewDataResponse.setDataSearch(loginSuccessResponse);
        }
        response.setAccessToken(jwt);
        response.setCurrentUser(currentUser);
        response.setAppType(appType);
        this.setAvatarUser(maUser, response);
        return NewDataResponse.setDataCustom(response, MessageWebConstant.LOGIN_SUCCESS);
    }

    private LoginMobileRequest getLoginRequest(LoginMobileRequest request) {
        request.setUsername(request.getUsername().trim());
        request.setPassword(request.getPassword().trim());
        request.setAppType(request.getAppType().trim());
        return request;
    }

    private LoginRequest getLoginWebRequest(LoginRequest request) {
        request.setUsername(request.getUsername().trim());
        request.setPassword(request.getPassword().trim());
        return request;
    }


    /**
     * set thông tin liên quan đén trường
     *
     * @param maUser
     * @param response
     */
    private void setInforSchool(UserPrincipal principal, MaUser maUser, JwtAuthenticationResponse response) {
        SchoolInforPayload model = new SchoolInforPayload();
        String appType = maUser.getAppType();
        if (appType.equals(AppTypeConstant.SCHOOL)) {
            Long idSchool = maUser.getEmployee().getIdSchoolLogin();
            School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
            model.setSchoolName(school.getSchoolName());
            //check có nhiều trường ứng với tài khoản đó hay không
            List<InfoEmployeeSchool> infoEmployeeSchoolList = UserPrincipleToUserUtils.getInfoEmployeeInMaUser(maUser);
            if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.EMPTY_USER_LOGIN);
            } else if (infoEmployeeSchoolList.size() > 1) {
                model.setManyStatus(AppConstant.APP_TRUE);
            }
        } else if (appType.equals(AppTypeConstant.TEACHER)) {
            InfoEmployeeSchool infoEmployeeSchool = UserPrincipleToUserUtils.getInfoEmployeeFromPrinciple(principal);
            School school = infoEmployeeSchool.getSchool();
            model.setSchoolName(school.getSchoolName());
            List<InfoEmployeeSchool> infoEmployeeSchoolList = UserPrincipleToUserUtils.getInfoEmployeeInMaUser(maUser);
            if (infoEmployeeSchoolList.size() > 1) {
                model.setManyStatus(AppConstant.APP_TRUE);
            }
        } else if (appType.equals(AppTypeConstant.PARENT)) {
            Long idLogin = principal.getIdKidLogin();
            List<Kids> kidsList = maUser.getParent().getKidsList().stream().filter(x -> x.isDelActive() && x.isActivated()).collect(Collectors.toList());
            model.setSchoolName(principal.getSchool().getSchoolName());
            response.setCurrentUser(kidsList.stream().filter(x -> x.getId().equals(idLogin)).collect(Collectors.toList()).get(0).getFullName());
            if (kidsList.size() > 1) {
                model.setManyStatus(AppConstant.APP_TRUE);
            }
        } else if (appType.equals(AppTypeConstant.SUPPER_SCHOOL)) {
            School school = maUser.getSchoolMaster().getSchool();
            model.setSchoolName(school.getSchoolName());
        }
        response.setSchoolInfor(model);
    }

    /**
     * set avatar
     *
     * @param maUser
     * @param response
     */
    private void setAvatarUser(MaUser maUser, JwtAuthenticationResponse response) {
        if (maUser.getAppType().equals(AppTypeConstant.SCHOOL)) {
            response.setAvatar(StringUtils.isNotBlank(maUser.getEmployee().getAvatar()) ? maUser.getEmployee().getAvatar() : AvatarDefaultConstant.AVATAR_SCHOOL);
        } else if (maUser.getAppType().equals(AppTypeConstant.TEACHER)) {
            response.setAvatar(StringUtils.isNotBlank(maUser.getEmployee().getAvatar()) ? maUser.getEmployee().getAvatar() : AvatarDefaultConstant.AVATAR_TEACHER);
        } else if (maUser.getAppType().equals(AppTypeConstant.PARENT)) {
            response.setAvatar(StringUtils.isNotBlank(maUser.getParent().getAvatar()) ? maUser.getParent().getAvatar() : AvatarDefaultConstant.AVATAR_PARENT);
        }
    }

}
