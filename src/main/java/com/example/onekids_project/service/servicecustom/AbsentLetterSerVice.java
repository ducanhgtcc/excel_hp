package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.AbsentLetterRequest;
import com.example.onekids_project.request.parentdiary.SearchAbsentLetterRequest;
import com.example.onekids_project.response.parentdiary.AbsentNewResponse;
import com.example.onekids_project.response.parentdiary.ListAbsentLetterResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

public interface AbsentLetterSerVice {

    ListAbsentLetterResponse searchAbsent(UserPrincipal principal, SearchAbsentLetterRequest request);

    boolean updateAbsent(Long idSchoolLogin, UserPrincipal principal, ContentRequest absentLetterEditRequest) throws FirebaseMessagingException;

    boolean confirmReply(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    boolean revokeTeacher(UserPrincipal principal, StatusRequest request);

    boolean revokePlus(UserPrincipal principal, StatusRequest request);

    AbsentNewResponse findByIdAbsent(UserPrincipal principal, Long id);

    boolean updateRead(Long id, List<AbsentLetterRequest> absentletterResponse);

    boolean updateConfirmMany(Long id, List<AbsentLetterRequest> absentletterResponse, UserPrincipal principal) throws FirebaseMessagingException;
}
