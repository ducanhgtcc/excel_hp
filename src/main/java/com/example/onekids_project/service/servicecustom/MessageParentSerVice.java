package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.MessageParentRequest;
import com.example.onekids_project.request.parentdiary.SearchMessageParentRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.parentdiary.ListMessageParentResponse;
import com.example.onekids_project.response.parentdiary.MessageNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface MessageParentSerVice {

    ListMessageParentResponse searchMessageParent(UserPrincipal principal, SearchMessageParentRequest request);

    MessageParentRequest updateRead(Long id, List<MessageParentRequest> messageParentResponse);

    boolean updateConfirmMany(Long id, UserPrincipal principal, List<MessageParentRequest> messageParentResponse) throws FirebaseMessagingException;

    boolean updateMessage(Long idSchoolLogin, UserPrincipal principal, ContentRequest absentLetterEditRequest) throws FirebaseMessagingException;

    boolean confirmReply(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    boolean revokeTeacher(UserPrincipal principal, StatusRequest request);

    boolean revokePlus(UserPrincipal principal, StatusRequest request);

    MessageNewResponse findByIdMessageNew(UserPrincipal principal, Long id);


}
