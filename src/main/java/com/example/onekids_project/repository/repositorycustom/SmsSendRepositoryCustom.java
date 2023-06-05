package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.SearchSmsPlusRequest;
import com.example.onekids_project.request.notifihistory.SearchHistorySmsSendNewtRequest;
import com.example.onekids_project.request.notifihistory.SearchSmsSendRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsSendRepositoryCustom {

    List<SmsSend> searchSmsSend(Long idSchool, SearchSmsSendRequest searchSmsSendRequest);

    List<SmsSend> findByStatusSend();

    List<SmsSend> searchHistorySmsSend(Long idSchool, SearchHistorySmsSendNewtRequest request);

    long coutSearchSmsSendHistory(Long idSchool, SearchHistorySmsSendNewtRequest request);

    List<SmsSend> searchSmsSendPlus(Long idSchool, SearchSmsPlusRequest request);
}
