package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.feedbackplus.FeedbackPlusRequest;
import com.example.onekids_project.mobile.plus.request.feedbackplus.FeedbackRevokeRequest;
import com.example.onekids_project.mobile.plus.response.feedbackplus.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface FeedbackPlusMobileService {


    ListFeedbackPlusResponse searchFeedbackPlus(UserPrincipal principal, FeedbackPlusRequest request);

    FeedbackPlusDetailResponse findDetailFeedback(UserPrincipal principal, Long id);

    FeedbackPlusRevokeResponse sendRevoke(UserPrincipal principal, FeedbackRevokeRequest request);

    FeedbackPlusConfirmResponse feedbackPlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    FeedbackPlusSendReplyResponse sendFeedbackReply(Long idSchoolLogin, UserPrincipal principal, UpdatePlusSendReplyRequest request) throws FirebaseMessagingException;
}
