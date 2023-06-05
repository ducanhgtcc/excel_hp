package com.example.onekids_project.service.servicecustom.parentweb;

import com.example.onekids_project.request.parentweb.NotifyParentWebRequest;
import com.example.onekids_project.response.parentweb.ListNotifyParentResponse;

import java.util.List;

/**
 * @author lavanviet
 */
public interface ParentNotifyService {
    ListNotifyParentResponse findNotifyWeb(NotifyParentWebRequest request);
    void viewNotifyWeb(List<Long> idList);
}
