package com.example.onekids_project.master.service;

import com.example.onekids_project.master.request.notify.NotificationManageRequest;
import com.example.onekids_project.master.request.notify.NotificationStatusRequest;
import com.example.onekids_project.master.request.notify.NotifyManageDateRequest;
import com.example.onekids_project.master.request.notify.SearchNotificationRequest;
import com.example.onekids_project.master.response.notify.ListNotifyManageResponse;
import com.example.onekids_project.master.response.notify.NotifyManageDateResponse;

/**
 * date 2021-08-02 9:49 AM
 *
 * @author nguyễn văn thụ
 */
public interface NotificationManageService {
    ListNotifyManageResponse findNotifyManage(Long idSchool, SearchNotificationRequest request);

    NotifyManageDateResponse findDateNotification(Long id);

    boolean updateDateNotification(NotifyManageDateRequest request);

    boolean deleteDateNotification(Long id);

    boolean createDateNotification(Long idSchool, NotifyManageDateRequest request);

    boolean getStatusNotifyManage(Long idSchool, NotificationStatusRequest request);

    boolean getUpdateNotifyManage(Long idSchool, NotificationManageRequest request);
}
