package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.teacher.request.notifyTeacher.CreateNotifyTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.SearchNotifyTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.notify.ListMobileNotifyTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.notify.MobileNotifiDetailTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface NotifyTeacherService {
    ListMobileNotifyTeacherResponse findNotifiTeacherForMobile(UserPrincipal principal, SearchNotifyTeacherRequest searchNotifyTeacherRequest);

    MobileNotifiDetailTeacherResponse findNotifiTeacherByIdForMobile(Long id, UserPrincipal principal);

    boolean createNotififorTeacherToParent(UserPrincipal principal, CreateNotifyTeacherRequest createNotifyTeacherRequest) throws IOException, FirebaseMessagingException;
}
