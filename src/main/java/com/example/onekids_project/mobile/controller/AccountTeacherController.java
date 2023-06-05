package com.example.onekids_project.mobile.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.response.account.AccountTeacherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AccountTeacherService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/mob/teacher/account")
public class AccountTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountTeacherService accountTeacherService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAccount(@CurrentUser UserPrincipal principal) {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        AccountTeacherResponse model = accountTeacherService.findInforAccount(principal);
        return NewDataResponse.setDataCustom(model, MessageConstant.FIND_ACCOUNT);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/avatar")
    public ResponseEntity saveAvatar(@CurrentUser UserPrincipal principal, @ModelAttribute MultipartFile multipartFile) throws IOException {
        logger.info("username: {}, fullName: {}, {}", principal.getUsername(), principal.getFullName());
        boolean checkSave = accountTeacherService.saveAvatar(principal, multipartFile);
        return NewDataResponse.setDataCustom(checkSave, MessageConstant.UPDATE_AVATAR);
    }


}
