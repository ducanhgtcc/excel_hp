package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusReplyRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.response.*;
import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.UpdateTeacherReplyRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.UpdateTeacherSendReplyRequest;
import com.example.onekids_project.mobile.teacher.response.message.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface MessagePlusMobileService {


    ListMessagePlusResponse searchMessagePlus(UserPrincipal principal, SearchMessagePlusRequest request);

    MessagePlusDetailResponse findDeTailPlusMessage(UserPrincipal principal, Long id);

    MessagePlusConfirmResponse messagePlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    MessagePlusSendReplyResponse sendPlusReply(Long idSchoolLogin, UserPrincipal principal, UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException;

    MessagePlusRevokeResponse sendRevoke(Long idSchoolLogin, UserPrincipal principal, UpdatePlusRevokeRequest request);
}
