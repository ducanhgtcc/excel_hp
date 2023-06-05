package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.response.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.MessagePlusMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/plus/message")
public class MessagePlusController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessagePlusMobileService messagePlusMobileService;

    /**
     * Lấy danh sách
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchMessagePlus(@CurrentUser UserPrincipal principal, @Valid SearchMessagePlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListMessagePlusResponse listMessagePlusResponse = messagePlusMobileService.searchMessagePlus(principal, request);
        return NewDataResponse.setDataSearch(listMessagePlusResponse);
    }

    /**
     * Xem chi tiết
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMessagePlusDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        MessagePlusDetailResponse messagePlusDetailResponse = messagePlusMobileService.findDeTailPlusMessage(principal, id);
        return NewDataResponse.setDataSearch(messagePlusDetailResponse);
    }

    /**
     * Xác nhận
     *
     * @param principal
     * @param id
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity ConfirmTeacherReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, id);
        MessagePlusConfirmResponse messagePlusConfirmResponse = messagePlusMobileService.messagePlusConfirm(principal, id);
        return NewDataResponse.setDataCustom(messagePlusConfirmResponse, MessageConstant.MESSAGE_CONFIRM);
    }

    /**
     * Gửi phản hồi
     *
     * @param principal
     * @param updatePlusSendReplyRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/reply")
    public ResponseEntity sendReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, updatePlusSendReplyRequest);
        MessagePlusSendReplyResponse messagePlusSendReplyResponse = messagePlusMobileService.sendPlusReply(principal.getIdSchoolLogin(), principal, updatePlusSendReplyRequest);
        return NewDataResponse.setDataCustom(messagePlusSendReplyResponse, MessageConstant.MESSAGE_SENDREPLY);
    }

    /**
     * Thu hồi
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke")
    public ResponseEntity sendRevoke(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdatePlusRevokeRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        MessagePlusRevokeResponse messagePlusRevokeResponse = messagePlusMobileService.sendRevoke(principal.getIdSchoolLogin(), principal, request);
        return NewDataResponse.setDataCustom(messagePlusRevokeResponse, MessageConstant.REVOKE);
    }

}
