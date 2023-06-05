package com.example.onekids_project.controller.user;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.master.request.school.SearchAccountRequest;
import com.example.onekids_project.request.mauser.*;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.mauser.*;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/web/user")
public class HandleDuplicateController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MaUserService maUserService;

    /**
     * tìm kiếm trang cá nhân
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/profile")
    public ResponseEntity getProfile(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        ProfileResponse data = maUserService.findProfile(principal);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.FIND_PROFILE);
    }

    /**
     * cập nhật trang cá nhân
     *
     * @param principal
     * @param profileRequest
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/profile")
    public ResponseEntity updateProfile(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute ProfileRequest profileRequest) throws IOException {
        RequestUtils.getFirstRequest(principal, profileRequest);
        boolean data = maUserService.updateProfile(principal, profileRequest);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.UPDATE_PROFILE);
    }

    /**
     * check sự tồn tại của username và appType tương ứng
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/account/check-exist")
    public ResponseEntity checkAccountExist(@CurrentUser UserPrincipal principal, @Valid @RequestBody UsernameRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        maUserService.checkAccountHandle(request);
        return NewDataResponse.setDataCustom(AppConstant.APP_TRUE, MessageWebConstant.USERNAME_NOT_EXIST);
    }

    /**
     * câp nhật số điện thoại mới cho tài khoản ở trạng thái xử lý
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/account/update-newphone")
    public ResponseEntity updateNewPhone(@CurrentUser UserPrincipal principal, @Valid @RequestBody HandleNewPhoneRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        boolean check = maUserService.createAccountWithNewPhone(request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.UPDATE_NEW_PHONE);
    }

    /**
     * kiểm tra mã xác thực
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/account/verification-account")
    public ResponseEntity verificationAccount(@CurrentUser UserPrincipal principal, @Valid @RequestBody VerificationAccountRequest request) {
        RequestUtils.getFirstRequest(principal, request);
        boolean check = maUserService.verifycation(principal, request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.VALID_VERIFY_CODE);
    }

    /**
     * gộp tài khoản: sử dụng tài khoản cũ hoặc tạo tài khoản mới
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/account/merge-account")
    public ResponseEntity mergeAccount(@CurrentUser UserPrincipal principal, @Valid @RequestBody MergeAccountRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        boolean check = maUserService.mergeAccount(request);
        return NewDataResponse.setDataCustom(check, MessageWebConstant.UPDATE_NEW_PHONE);
    }

    /**
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/account/duplicate-employee")
    public ResponseEntity getAccountDuplicateEmployee(@CurrentUser UserPrincipal principal, @Valid AccountInforRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        List<AccountEmployeeHandleResponse> responseList = maUserService.getAccountEmployeeDuplicate(principal, request);
        return NewDataResponse.setDataSearch(responseList);
    }

    /**
     * @param principal
     * @param phone
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/account/duplicate-kids")
    public ResponseEntity getAccountDuplicateKids(@CurrentUser UserPrincipal principal, String phone) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), phone);
        List<AccountKidsHandleResponse> responseList = maUserService.getAccountKidsDuplicate(principal, phone);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/username")
    public ResponseEntity getParentByUsername(@CurrentUser UserPrincipal principal, @Valid AccountInforRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        MaUserParentResponse response = maUserService.getMauserByUsername(request.getPhone(), request.getAppType());
        return NewDataResponse.setDataSearch(response);
    }

}
