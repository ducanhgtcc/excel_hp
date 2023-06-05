package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.request.absentteacher.AbsentTeacherMobileRequest;
import com.example.onekids_project.mobile.teacher.response.absentteacher.AbsentTeacherDetailMobileResponse;
import com.example.onekids_project.mobile.teacher.response.absentteacher.ListAbsentTeacherMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.IOException;

/**
 * date 2021-05-21 5:17 PM
 *
 * @author nguyễn văn thụ
 */
public interface AbsentTeacherMobileService {

    boolean createAbsentTeacher(UserPrincipal userPrincipal, AbsentTeacherMobileRequest absentTeacherMobileRequest) throws FirebaseMessagingException, IOException;

    ListAbsentTeacherMobileResponse searchAbsentTeacher(UserPrincipal principal, PageNumberRequest request);

    boolean absentTeacherRevoke(Long id);

    AbsentTeacherDetailMobileResponse findAbsentTeacherDetail(UserPrincipal principal, Long id);
}
