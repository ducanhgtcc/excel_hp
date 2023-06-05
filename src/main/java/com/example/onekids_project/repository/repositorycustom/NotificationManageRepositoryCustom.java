package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.NotificationManage;
import com.example.onekids_project.master.request.notify.SearchNotificationRequest;

import java.util.List;

/**
 * date 2021-08-02 10:11 AM
 *
 * @author nguyễn văn thụ
 */
public interface NotificationManageRepositoryCustom {

    List<NotificationManage> findNotificationManage(Long idSchool, SearchNotificationRequest request);
    long countNotificationManage(Long idSchool, SearchNotificationRequest request);
}
