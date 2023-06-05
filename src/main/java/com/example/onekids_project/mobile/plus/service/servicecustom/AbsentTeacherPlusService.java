package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.absentteacher.SearchAbsentTeacherPlusRequest;
import com.example.onekids_project.mobile.plus.response.absentteacher.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

/**
 * date 2021-05-31 8:53 AM
 *
 * @author nguyễn văn thụ
 */
public interface AbsentTeacherPlusService {

    ListAbsentTeacherPlusResponse searchAbsentTeacherPlus(UserPrincipal principal, SearchAbsentTeacherPlusRequest request);

    AbsentTeacherPlusDetailResponse findDeTailPlusAbsentTeacher(UserPrincipal principal, Long id);

    AbsentTeacherPlusRevokeResponse sendRevoke(UserPrincipal principal, Long id);

    AbsentTeacherPlusConfirmResponse absentTeacherPlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    AbsentTeacherPlusSendReplyResponse sendPlusReply(UserPrincipal principal, UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException;
}
