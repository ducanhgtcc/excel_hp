package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.notify.NotifyPlusRequest;
import com.example.onekids_project.mobile.plus.response.notify.NotifyPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.NotifyPlusService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/mob/plus/notify")
public class NotifyPlusController {

    @Autowired
    private NotifyPlusService notifyPlusService;


    /**
     * tạo thông báo cho phòng ban
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-notify-deparment")
    public ResponseEntity createNotifyDeparment(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        NotifyPlusResponse response = notifyPlusService.createNotifyDeparment(principal, request);
        if (response.isCheck()) {
            return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_EMPLOYEE);
        }
        return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_FAIL);
    }

    /**
     * tạo thông báo cho phòng ban
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-notify-employee")
    public ResponseEntity createNotifyEmployee(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        NotifyPlusResponse response = notifyPlusService.createNotifyEmployee(principal, request);
        if (response.isCheck()) {
            return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_EMPLOYEE);
        }
        return NewDataResponse.setData(response, MessageConstant.CREATE_NOTIFY_FAIL, HttpStatus.BAD_GATEWAY);
    }

    /**
     * tạo thông báo cho phòng ban
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-notify-grade")
    public ResponseEntity createNotifyGrade(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        NotifyPlusResponse response = notifyPlusService.createNotifyGrade(principal, request);
        if (response.isCheck()) {
            return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_KID);
        }
        return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_FAIL);
    }


    /**
     * tạo thông báo cho phòng ban
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-notify-class")
    public ResponseEntity createNotifyClass(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        NotifyPlusResponse response = notifyPlusService.createNotifyClass(principal, request);
        if (response.isCheck()) {
            return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_KID);
        }
        return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_FAIL);
    }


    /**
     * tạo thông báo cho phòng ban
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-notify-student")
    public ResponseEntity createNotifyStudent(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        NotifyPlusResponse response = notifyPlusService.createNotifyStudent(principal, request);
        if (response.isCheck()) {
            return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_KID);
        }
        return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_FAIL);
    }

    /**
     * tạo thông báo cho phòng banp
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create-notify-group")
    public ResponseEntity createNotifyGroup(@CurrentUser UserPrincipal principal, @ModelAttribute @Valid NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        NotifyPlusResponse response = notifyPlusService.createNotifyGroup(principal, request);
        if (response.isCheck()) {
            return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_KID);
        }
        return NewDataResponse.setDataCustom(response, MessageConstant.CREATE_NOTIFY_FAIL);
    }
}
