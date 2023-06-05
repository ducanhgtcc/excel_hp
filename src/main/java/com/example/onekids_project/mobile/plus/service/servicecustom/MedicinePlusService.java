package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.SearchMessagePlusRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusReplyRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusRevokeRequest;
import com.example.onekids_project.mobile.plus.request.UpdatePlusSendReplyRequest;
import com.example.onekids_project.mobile.plus.request.medicine.SearchMedicinePlusRequest;
import com.example.onekids_project.mobile.plus.response.*;
import com.example.onekids_project.mobile.plus.response.medicine.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface MedicinePlusService {

    ListMedicinePlusResponse searchMedicinePlus(UserPrincipal principal, SearchMedicinePlusRequest request);

    MedicinePlusDetailResponse findDetailMedicinePlus(UserPrincipal principal, Long id);

    MedicinePlusConfirmResponse medicinePlusConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    MedicinePlusSendReplyResponse sendPlusReply(Long idSchoolLogin, UserPrincipal principal, UpdatePlusSendReplyRequest updatePlusSendReplyRequest) throws FirebaseMessagingException;

    MedicinePlusRevokeResponse sendRevoke(Long idSchoolLogin, UserPrincipal principal, UpdatePlusRevokeRequest request);
}
