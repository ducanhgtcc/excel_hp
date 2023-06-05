package com.example.onekids_project.master.controller;

import com.example.onekids_project.common.AccountTypeConstant;
import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.master.request.school.SearchAccountRequest;
import com.example.onekids_project.request.mauser.AccountRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.mauser.ListAccountResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * date 2021-09-24 17:22
 *
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/account")
public class AccountController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MaUserService maUserService;

    /**
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAccountUser(@CurrentUser UserPrincipal principal, @Valid SearchAccountRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        ListAccountResponse data = maUserService.findAccount(request);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.FIND_ACCOUNT);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateAccountUser(@CurrentUser UserPrincipal principal, @Valid @RequestBody AccountRequest request) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), request);
        boolean data = maUserService.updateAccount(request);
        return NewDataResponse.setDataCustom(data, MessageWebConstant.UPDATE_ACCOUNT);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity deleteAccountUser(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        boolean data = maUserService.deleteAccount(id);
        return NewDataResponse.setDataDelete(data);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/complete/{id}")
    public ResponseEntity deleteCompleteAccountUser(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        maUserService.deleteCompleteAccount(id, AccountTypeConstant.DELETE_MANUAL);
        return NewDataResponse.setDataDelete(AppConstant.APP_TRUE);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/restore/{id}")
    public ResponseEntity restoreAccountUser(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName(), id);
        boolean data = maUserService.restoreAccount(id);
        return NewDataResponse.setDataRestore(data);
    }
}
