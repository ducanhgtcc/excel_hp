package com.example.onekids_project.master.service;

import com.example.onekids_project.master.request.notify.SearchInternalNotificationPlus;
import com.example.onekids_project.master.response.notify.InternalNotificationPlusResponse;
import com.example.onekids_project.master.response.notify.ListInternalNotificationPlusResponse;

/**
 * date 2021-08-12 2:21 PM
 *
 * @author nguyễn văn thụ
 */
public interface InternalNotificationPlusService {

    ListInternalNotificationPlusResponse findInternalNotificationPlus(SearchInternalNotificationPlus request);

    InternalNotificationPlusResponse findByIdNotificationPlus(Long id);
}
