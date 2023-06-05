package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.request.notifihistory.SearchSmsSendHistoryRequest;

import java.util.List;

public interface SmsSendHistoryRepositoryCustom {


    List<SmsSend> searchSmsSendCustom(SearchSmsSendHistoryRequest searchSmsSendHistoryRequest);
}
