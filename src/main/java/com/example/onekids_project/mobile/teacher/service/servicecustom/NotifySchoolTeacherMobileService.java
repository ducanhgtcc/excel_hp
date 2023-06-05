package com.example.onekids_project.mobile.teacher.service.servicecustom;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.mobile.teacher.response.notifyschool.ListNotifySchoolTeacherResponse;

/**
 * date 2021-10-22 10:53 AM
 *
 * @author nguyễn văn thụ
 */
public interface NotifySchoolTeacherMobileService {

    ListNotifySchoolTeacherResponse searchNotifySchoolTeacher(Long idSchool, PageNumberRequest request);
}
