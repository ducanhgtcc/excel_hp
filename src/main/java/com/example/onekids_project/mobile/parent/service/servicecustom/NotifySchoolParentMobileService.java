package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.notifyschool.ListNotifySchoolParentResponse;
import com.example.onekids_project.mobile.request.PageNumberRequest;

/**
 * date 2021-10-22 10:13 AM
 *
 * @author nguyễn văn thụ
 */
public interface NotifySchoolParentMobileService {

    ListNotifySchoolParentResponse searchNotifySchool(Long idSchool, PageNumberRequest request);
}
