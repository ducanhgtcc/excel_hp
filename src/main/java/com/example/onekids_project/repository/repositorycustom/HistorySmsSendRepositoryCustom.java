package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.request.notifihistory.SearchHistorySmsSendNewtRequest;

import java.util.List;

public interface HistorySmsSendRepositoryCustom {

 List<SmsReceivers> searchHistorySmsSend(Long idSchool, SearchHistorySmsSendNewtRequest request);

 long coutSearchSmsSendHistory(Long idSchool, SearchHistorySmsSendNewtRequest request);
}


