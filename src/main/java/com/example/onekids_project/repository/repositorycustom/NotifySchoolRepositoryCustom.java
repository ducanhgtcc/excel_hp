package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.NotifySchool;
import com.example.onekids_project.mobile.plus.request.notifyschool.SearchNotifySchoolPlusRequest;
import com.example.onekids_project.mobile.request.PageNumberRequest;
import com.example.onekids_project.request.notifyschool.SearchNotifySchoolRequest;

import java.util.List;

/**
 * date 2021-10-21 10:00 AM
 *
 * @author nguyễn văn thụ
 */
public interface NotifySchoolRepositoryCustom {

    List<NotifySchool> searchNotifySchool(Long idSchool, SearchNotifySchoolRequest request);

    long countNotifySchool(Long idSchool, SearchNotifySchoolRequest request);

    List<NotifySchool> searchNotifySchoolParentMobile(Long idSchool, PageNumberRequest request);

    List<NotifySchool> searchNotifySchoolTeacherMobile(Long idSchool, PageNumberRequest request);

    List<NotifySchool> searchNotifySchoolPlusMobile(Long idSchool, SearchNotifySchoolPlusRequest request);
}
