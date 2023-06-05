package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.InternalNotificationPlus;
import com.example.onekids_project.master.request.notify.SearchInternalNotificationPlus;

import java.util.List;

/**
 * date 2021-08-12 2:34 PM
 *
 * @author nguyễn văn thụ
 */
public interface InternalNotificationPlusCustomRepository {

    List<InternalNotificationPlus> findInternalPlus(SearchInternalNotificationPlus request);
    long countInternalPlus(SearchInternalNotificationPlus request);
}
