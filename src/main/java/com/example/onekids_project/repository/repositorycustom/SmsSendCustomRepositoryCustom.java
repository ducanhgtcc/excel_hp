package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.SmsSendCustom;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.SearchSmsPlusRequest;
import com.example.onekids_project.request.notifihistory.SearchSmsSendCustomRequest;

import java.util.List;

public interface SmsSendCustomRepositoryCustom {

    List<SmsSendCustom> searchSmsCustom(Long idSchool, SearchSmsSendCustomRequest request);

    long coutSearchSmsCustom(Long idSchool, SearchSmsSendCustomRequest request);

    List<SmsSendCustom> searchSmsSendCustom(Long idSchool, SearchSmsPlusRequest request);
}


