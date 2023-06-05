package com.example.onekids_project.mobile.parent.controller;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.parent.request.messageparent.MessageParentMobileRequest;
import com.example.onekids_project.mobile.parent.response.message.ListMessageParentMobileResponse;
import com.example.onekids_project.mobile.parent.response.message.MessageParentDetailMobileResponse;
import com.example.onekids_project.mobile.parent.service.servicecustom.MessageParentMobileService;
import com.example.onekids_project.mobile.request.DateTimeRequest;
import com.example.onekids_project.response.common.DataResponse;
import com.example.onekids_project.response.common.ErrorResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/mob/parent/message")
public class MessageParentMobileController {

    @Autowired
    private MessageParentMobileService messageParentMobileService;

    /**
     * tìm kiếm danh sách lời nhắn
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity searchMessageParent(@CurrentUser UserPrincipal principal, DateTimeRequest localDateTime) {
        RequestUtils.getFirstRequest(principal, localDateTime);
        CommonValidate.checkDataParent(principal);
        Pageable pageable = PageRequest.of(1, AppConstant.MAX_PAGE_ITEM);
        ListMessageParentMobileResponse listMessageParentMobileResponse = messageParentMobileService.findMessageParentMobile(principal, pageable, localDateTime.getDateTime());
        if (listMessageParentMobileResponse == null) {
            return NewDataResponse.setDataCustom(listMessageParentMobileResponse,"Không có lời nhắn nào cho phụ huynh");
        }
        return NewDataResponse.setDataSearch(listMessageParentMobileResponse);

    }

    /**
     * thu hồi lời nhắn
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity revokeMessageParent(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        boolean checkUpdate = messageParentMobileService.messageParentRevoke(id);
        if (!checkUpdate) {
            return NewDataResponse.setDataCustom(checkUpdate,"Lỗi thu hồi lời nhắn");
        }
        return NewDataResponse.setDataRevoke(checkUpdate);
    }

    /**
     * xem chi tiết lời nhắn
     *
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMessageParentDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        CommonValidate.checkDataParent(principal);
        MessageParentDetailMobileResponse messageParentDetailMobileResponse = messageParentMobileService.findMessParentDetailMobile(principal, id);
        return NewDataResponse.setDataSearch(messageParentDetailMobileResponse);

    }

    /**
     * create messge parent
     *
     * @param principal
     * @param messageParentMobileRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity createMessageParent(@CurrentUser UserPrincipal principal, @Valid @ModelAttribute MessageParentMobileRequest messageParentMobileRequest) throws FirebaseMessagingException, IOException {
        RequestUtils.getFirstRequest(principal, messageParentMobileRequest);
        CommonValidate.checkDataParent(principal);
        boolean checkCreate = messageParentMobileService.createMessageParent(principal, messageParentMobileRequest);
        if (!checkCreate) {
            return NewDataResponse.setDataCustom(checkCreate,"Lỗi tạo lời nhắn");
        }
        return NewDataResponse.setDataCreate(checkCreate);
    }
}
