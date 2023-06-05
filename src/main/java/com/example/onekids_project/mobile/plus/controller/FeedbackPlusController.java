package com.example.onekids_project.mobile.plus.controller;

import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.feedbackplus.FeedbackPlusRequest;
import com.example.onekids_project.mobile.plus.request.feedbackplus.FeedbackRevokeRequest;
import com.example.onekids_project.mobile.plus.response.feedbackplus.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.FeedbackPlusMobileService;
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
@RequestMapping("/mob/plus/feedback")
public class FeedbackPlusController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedbackPlusMobileService feedbackPlusMobileService;

    /**
     * Danh sách góp ý
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    public ResponseEntity searchMessagePlus(@CurrentUser UserPrincipal principal, @Valid FeedbackPlusRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        ListFeedbackPlusResponse response = feedbackPlusMobileService.searchFeedbackPlus(principal, request);
        return NewDataResponse.setDataSearch(response);
    }

    /**
     * Xem chi tiết góp ý
     *
     * @param principal
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity findMessagePlusDetail(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) {
        RequestUtils.getFirstRequestPlus(principal, id);
        FeedbackPlusDetailResponse feedbackPlusDetailResponse = feedbackPlusMobileService.findDetailFeedback(principal, id);
        return NewDataResponse.setDataSearch(feedbackPlusDetailResponse);
    }

    /**
     * Thu hồi góp ý
     *
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/revoke")
    public ResponseEntity sendRevoke(@CurrentUser UserPrincipal principal, @RequestBody FeedbackRevokeRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        FeedbackPlusRevokeResponse feedbackPlusRevokeResponse = feedbackPlusMobileService.sendRevoke(principal, request);
        return NewDataResponse.setDataCustom(feedbackPlusRevokeResponse, MessageConstant.REVOKE);
    }

    /**
     * Xác nhận cho parent và teacher
     *
     * @param principal
     * @param id
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/confirm/{id}")
    public ResponseEntity ConfirmTeacherReply(@CurrentUser UserPrincipal principal, @PathVariable("id") Long id) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, id);
        FeedbackPlusConfirmResponse feedbackPlusConfirmResponse = feedbackPlusMobileService.feedbackPlusConfirm(principal, id);
        return NewDataResponse.setDataCustom(feedbackPlusConfirmResponse, MessageConstant.FEEDBACK_CONFIRM);
    }

    /**
     * Gửi phản hồi cho parent và teacher
     *
     * @param principal
     * @param request
     * @return
     * @throws FirebaseMessagingException
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/reply")
    public ResponseEntity sendReply(@CurrentUser UserPrincipal principal, @Valid @RequestBody UpdatePlusSendReplyRequest request) throws FirebaseMessagingException {
        RequestUtils.getFirstRequestPlus(principal, request);
        FeedbackPlusSendReplyResponse feedbackPlusSendReplyResponse = feedbackPlusMobileService.sendFeedbackReply(principal.getIdSchoolLogin(), principal, request);
        return NewDataResponse.setDataCustom(feedbackPlusSendReplyResponse, MessageConstant.MESSAGE_SENDREPLY);
    }

}
