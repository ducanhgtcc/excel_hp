package com.example.onekids_project.mobile.teacher.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.teacher.request.absent.SearchAbsentTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.absent.UpdateTeacherReplyabsentRequest;
import com.example.onekids_project.mobile.teacher.response.absentletter.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.AbsentLetterTeacherMobileService;
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
@RequestMapping("/mob/teacher/absent")
public class AbsentLetterTeacherController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AbsentLetterTeacherMobileService absentLetterTeacherMobileService;

    /**
     * Danh sách xin nghỉ
     *
     * @param principal
     * @param searchAbsentTeacherRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchAbsentTeacher(@CurrentUser UserPrincipal principal, @Valid SearchAbsentTeacherRequest searchAbsentTeacherRequest) {
        RequestUtils.getFirstRequest(principal, searchAbsentTeacherRequest);
        ListAbsentTeacherResponse listAbsentTeacherResponse = absentLetterTeacherMobileService.searchAbsentTeacher(principal, searchAbsentTeacherRequest);
        return NewDataResponse.setDataCustom(listAbsentTeacherResponse, MessageConstant.SEARCH_ABSENT);
    }

    /**
     * Xem chi tiết
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/detail/{id}")
    public ResponseEntity findAbsentTeacherDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        AbsentTeacherDetailResponse absentTeacherDetailResponse = absentLetterTeacherMobileService.findAbsentLetterDetail(principal, id);
        return NewDataResponse.setDataCustom(absentTeacherDetailResponse, MessageConstant.SEARCH_ABSENT_DETAIL);
    }

    /**
     * Thu hồi phản hồi
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke/{id}")
    public ResponseEntity revokeAbsent(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequest(principal, id);
        AbsentTeacherRevokeResponse absentTeacherRevokeResponse = absentLetterTeacherMobileService.absentTeacherRevoke(principal, id);
        return NewDataResponse.setDataCustom(absentTeacherRevokeResponse, MessageConstant.MESSAGE_REVOKE);
    }

    /**
     * Gửi phản hồi
     *
     * @param principal
     * @param updateTeacherSendReplyRequest
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/sendreply")
    public ResponseEntity sendReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateTeacherReplyabsentRequest updateTeacherSendReplyRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, updateTeacherSendReplyRequest);
        AbsentTeacherSendReplyResponse absentTeacherSendReplyResponse = absentLetterTeacherMobileService.sendTeacherReply(principal.getIdSchoolLogin(), principal, updateTeacherSendReplyRequest);
        return NewDataResponse.setDataCustom(absentTeacherSendReplyResponse, MessageConstant.MESSAGE_SENDREPLY);
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
        RequestUtils.getFirstRequest(principal, id);
        AbsentTeacheConfirmResponse absentTeacheConfirmResponse = absentLetterTeacherMobileService.absentTeacherConfirm(principal, id);
        return NewDataResponse.setDataCustom(absentTeacheConfirmResponse, MessageConstant.ABSENT_CONFIRM);
    }

    /**
     * Cập nhật
     *
     * @param principal
     * @param updateTeacherSendReplyRequest
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updatereply")
    public ResponseEntity updateReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdateTeacherReplyabsentRequest updateTeacherSendReplyRequest) throws FirebaseMessagingException {
        RequestUtils.getFirstRequest(principal, updateTeacherSendReplyRequest);
        AbsentTeacherSendReplyResponse absentTeacherSendReplyResponse = absentLetterTeacherMobileService.updateTeacherReply(principal.getIdSchoolLogin(), principal, updateTeacherSendReplyRequest);
        return NewDataResponse.setDataCustom(absentTeacherSendReplyResponse, MessageConstant.MESSAGE_UPDATEREPLY);
    }
}
