package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.absent.SearchAbsentPlusRequest;
import com.example.onekids_project.mobile.plus.response.absent.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface AbsentPlusMobileService {

    ListAbsentPlusResponse searchAbsentPlus(UserPrincipal principal, SearchAbsentPlusRequest request);

    AbsentPlusDetailResponse findDeTailPlusAbent(UserPrincipal principal, Long id);

    AbsentPlusConfirmResponse absentPlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    AbsentPlusSendReplyResponse sendPlusReply(Long idSchoolLogin, UserPrincipal principal, UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException;

    AbsentPlusRevokeResponse sendRevoke(Long idSchoolLogin, UserPrincipal principal, UpdatePlusRevokeRequest request);
}
