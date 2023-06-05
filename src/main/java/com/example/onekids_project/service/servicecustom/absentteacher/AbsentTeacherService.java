package com.example.onekids_project.service.servicecustom.absentteacher;

import com.example.onekids_project.request.absentteacher.AbsentTeacherRequest;
import com.example.onekids_project.request.absentteacher.SearchAbsentTeacherRequest;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.request.common.ContentRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.parentdiary.AbsentLetterRequest;
import com.example.onekids_project.response.absentteacher.AbsentTeacherDetailResponse;
import com.example.onekids_project.response.absentteacher.AbsentTeacherResponse;
import com.example.onekids_project.response.absentteacher.ListAbsentTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;

/**
 * date 2021-05-21 2:29 PM
 *
 * @author nguyễn văn thụ
 */
public interface AbsentTeacherService {

    ListAbsentTeacherResponse searchAbsentTeacher(UserPrincipal principal, SearchAbsentTeacherRequest request);

    AbsentTeacherDetailResponse findByIdAbsentTeacher(UserPrincipal principal, Long id);

    boolean confirmReply(UserPrincipal principal, Long id) throws FirebaseMessagingException;

    boolean updateConfirmMany(List<Long> request, UserPrincipal principal);

    boolean updateRead(List<Long> requestList);

    boolean updateAbsentTeacher(UserPrincipal principal, ContentRequest request) throws FirebaseMessagingException;

    boolean revokePlus(UserPrincipal principal, StatusRequest request);
}
