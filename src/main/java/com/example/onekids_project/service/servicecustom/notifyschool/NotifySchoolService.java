package com.example.onekids_project.service.servicecustom.notifyschool;

import com.example.onekids_project.request.notifyschool.NotifySchoolActiveRequest;
import com.example.onekids_project.request.notifyschool.NotifySchoolRequest;
import com.example.onekids_project.request.notifyschool.SearchNotifySchoolRequest;
import com.example.onekids_project.response.notifyschool.ListNotifySchoolResponse;
import com.example.onekids_project.response.notifyschool.NotifySchoolResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;

/**
 * date 2021-10-21 9:12 AM
 *
 * @author nguyễn văn thụ
 */
public interface NotifySchoolService {

    ListNotifySchoolResponse searchNotifySchool(Long idSchool, SearchNotifySchoolRequest request);

    NotifySchoolResponse findByNotifySchool(Long id);

    boolean saveNotifySchool(UserPrincipal principal, NotifySchoolRequest request) throws FirebaseMessagingException;

    boolean deleteNotifySchool(Long id);

    boolean activeNotifySchool(NotifySchoolActiveRequest request);
}
