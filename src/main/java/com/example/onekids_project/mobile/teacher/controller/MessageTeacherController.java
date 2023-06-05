package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.UpdateTeacherReplyRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.UpdateTeacherSendReplyRequest;
import com.example.onekids_project.mobile.teacher.response.message.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.MessageTeacherMobileService;
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
@RequestMapping("/mob/teacher/message")
public class MessageTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageTeacherMobileService messageTeacherService;

    /**
     * Danh sách lời nhắn
     * @param principal
     * @param searchMessageTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchmessageTeacher(@CurrentUser UserPrincipal principal, @Valid SearchMessageTeacherRequest searchMessageTeacherRequest) {
        RequestUtils.getFirstRequest(principal,searchMessageTeacherRequest);
        ListMessageTeacherResponse listMessageTeacherResponse = messageTeacherService.searchMessageTeache(principal, searchMessageTeacherRequest);
        return NewDataResponse.setDataSearch(listMessageTeacherResponse);
    }

    /**
     * Xem chi tiết
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findMessageTeacherDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        MessageTeacherDetailResponse messageTeacherDetailResponse = messageTeacherService.findDetailMessageTeacher(principal, id);
        return NewDataResponse.setDataSearch(messageTeacherDetailResponse);
    }

    /**
     * Thu hồi
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity revokeTeacherReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal,id);
        MessageTeacherRevokeResponse messageTeacherRevokeResponse = messageTeacherService.messageTeacherRevoke(principal, id);
        return NewDataResponse.setDataCustom(messageTeacherRevokeResponse, MessageConstant.MESSAGE_REVOKE);
    }

    /**
     * Gửi phản hồi
     * @param principal
     * @param updateTeacherSendReplyRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/sendreply")
    public ResponseEntity sendReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateTeacherSendReplyRequest updateTeacherSendReplyRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,updateTeacherSendReplyRequest);
        MessageTeacherSendReplyResponse messageTeacherSendReplyResponse = messageTeacherService.sendTeacherReply(principal.getIdSchoolLogin(), principal, updateTeacherSendReplyRequest);
        return NewDataResponse.setDataCustom(messageTeacherSendReplyResponse, MessageConstant.MESSAGE_SENDREPLY);
    }

    /**
     * Xác nhận
     * @param principal
     * @param id
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity ConfirmTeacherReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,id);
        MessageTeacheConfirmResponse messageTeacheConfirmResponse = messageTeacherService.messageTeacherConfirm(principal, id);
        return NewDataResponse.setDataCustom(messageTeacheConfirmResponse, MessageConstant.MESSAGE_CONFIRM);
    }

    /**
     * Cập nhật
     * @param principal
     * @param updateTeacherReplyRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updatereply")
    public ResponseEntity updateReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateTeacherReplyRequest updateTeacherReplyRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal,updateTeacherReplyRequest);
        MessageTeacherSendReplyResponse messageTeacherSendReplyResponse = messageTeacherService.updateTeacherReply(principal.getIdSchoolLogin(), principal, updateTeacherReplyRequest);
        return NewDataResponse.setDataCustom(messageTeacherSendReplyResponse, MessageConstant.MESSAGE_UPDATEREPLY);
    }
}
