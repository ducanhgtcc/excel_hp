package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.absent.SearchAbsentPlusRequest;
import com.example.onekids_project.mobile.plus.response.absent.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.AbsentPlusMobileService;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.RequestUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mob/plus/absent")
public class AbsentPlusController {

    @Autowired
    private AbsentPlusMobileService absentPlusMobileService;

    /**
     * Danh sách xin nghỉ
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchMessagePlus(@CurrentUser UserPrincipal principal, @Valid SearchAbsentPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListAbsentPlusResponse listAbsentPlusResponse = absentPlusMobileService.searchAbsentPlus(principal, request);
        return NewDataResponse.setDataSearch(listAbsentPlusResponse);
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
        RequestUtils.getFirstRequestPlus(principal);
        AbsentPlusDetailResponse absentPlusDetailResponse = absentPlusMobileService.findDeTailPlusAbent(principal, id);
        return NewDataResponse.setDataSearch(absentPlusDetailResponse);
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
        RequestUtils.getFirstRequestPlus(principal);
        AbsentPlusConfirmResponse absentPlusConfirmResponse = absentPlusMobileService.absentPlusConfirm(principal, id);
        return NewDataResponse.setDataCustom(absentPlusConfirmResponse, MessageConstant.ABSENT_CONFIRM);
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
        AbsentPlusSendReplyResponse absentPlusSendReplyResponse = absentPlusMobileService.sendPlusReply(principal.getIdSchoolLogin(), principal, updatePlusSendReplyRequest);
        return NewDataResponse.setDataCustom(absentPlusSendReplyResponse, MessageConstant.MESSAGE_SENDREPLY);
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
        AbsentPlusRevokeResponse absentPlusRevokeResponse = absentPlusMobileService.sendRevoke(principal.getIdSchoolLogin(), principal, request);
        return NewDataResponse.setDataCustom(absentPlusRevokeResponse, MessageConstant.REVOKE);
    }

}
