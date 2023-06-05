package com.example.onekids_project.mobile.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.notifyschool.NotifySchoolActivePlusRequest;
import com.example.onekids_project.mobile.plus.request.notifyschool.NotifySchoolPlusRequest;
import com.example.onekids_project.mobile.plus.request.notifyschool.SearchNotifySchoolPlusRequest;
import com.example.onekids_project.mobile.plus.response.notifyschool.ListNotifySchoolPlusResponse;
import com.example.onekids_project.mobile.plus.response.notifyschool.NotifySchoolPlusResponse;
import com.example.onekids_project.security.model.UserPrincipal;

/**
 * date 2021-10-22 11:12 AM
 *
 * @author nguyễn văn thụ
 */
public interface NotifySchoolPlusService {

    ListNotifySchoolPlusResponse searchNotifySchoolPlus(Long idSchool, SearchNotifySchoolPlusRequest request);

    NotifySchoolPlusResponse findByNotifySchoolPlus(Long id);

    boolean saveNotifySchoolPlus(UserPrincipal principal, NotifySchoolPlusRequest request);

    boolean deleteNotifySchoolPlus(Long id);

    boolean activeNotifySchoolPlus(NotifySchoolActivePlusRequest request);
}
