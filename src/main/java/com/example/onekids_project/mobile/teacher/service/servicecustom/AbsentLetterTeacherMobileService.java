package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.request.SearchMessageTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.UpdateTeacherReplyRequest;
import com.example.onekids_project.mobile.teacher.request.absent.SearchAbsentTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.absent.UpdateTeacherReplyabsentRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.UpdateTeacherSendReplyRequest;
import com.example.onekids_project.mobile.teacher.response.absentletter.*;
import com.example.onekids_project.mobile.teacher.response.message.ListMessageTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.message.MessageTeacherDetailResponse;
import com.example.onekids_project.mobile.teacher.response.message.MessageTeacherSendReplyResponse;
import com.example.onekids_project.request.parentdiary.SearchAbsentLetterRequest;
import com.example.onekids_project.response.parentdiary.MessageParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface AbsentLetterTeacherMobileService {


    ListAbsentTeacherResponse searchAbsentTeacher(UserPrincipal principal, SearchAbsentTeacherRequest searchAbsentTeacherRequest);

    AbsentTeacherDetailResponse findAbsentLetterDetail(UserPrincipal principal, Long id);

    AbsentTeacherRevokeResponse absentTeacherRevoke(UserPrincipal principal, Long id);

    AbsentTeacheConfirmResponse absentTeacherConfirm(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    AbsentTeacherSendReplyResponse sendTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherReplyabsentRequest updateTeacherSendReplyRequest) throws FirebaseMessagingException;

    AbsentTeacherSendReplyResponse updateTeacherReply(Long idSchoolLogin, UserPrincipal principal, UpdateTeacherReplyabsentRequest updateTeacherSendReplyRequest) throws FirebaseMessagingException;
}
