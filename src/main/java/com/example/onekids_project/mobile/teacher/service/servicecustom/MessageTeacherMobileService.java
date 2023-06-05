package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.UpdateTeacherReplyRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.UpdateTeacherSendReplyRequest;
import com.example.onekids_project.mobile.teacher.response.message.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface MessageTeacherMobileService {

    ListMessageTeacherResponse searchMessageTeache(UserPrincipal principal, SearchMessageTeacherRequest searchMessageTeacherRequest);

    MessageTeacherDetailResponse findDetailMessageTeacher(UserPrincipal principal, Long id);

    MessageTeacherRevokeResponse messageTeacherRevoke(UserPrincipal principal, Long id);

    MessageTeacheConfirmResponse messageTeacherConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    MessageTeacherSendReplyResponse sendTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherSendReplyRequest updateTeacherSendReplyRequest) throws FirebaseMessagingException;

    MessageTeacherSendReplyResponse updateTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherReplyRequest updateTeacherReplyRequest) throws FirebaseMessagingException;
}
