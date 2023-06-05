package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.mobile.teacher.response.historynotifi.HistorySmsSendNotifiResponse;
import com.example.onekids_project.request.notifihistory.SearchHistorySmsSendNewtRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.notifihistory.ListHistorySmsSendResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

//import org.springframework.data.domain.Pageable;

public interface HistorySmsSendService {

    ListHistorySmsSendResponse searchHistorySmsSend(UserPrincipal principal, SearchHistorySmsSendNewtRequest request);

    List<HistorySmsSendNotifiResponse> findDetailHistory(UserPrincipal principal, Long id);

    List<HistorySmsSendNotifiResponse> findDetailHistoryFail(UserPrincipal principal, Long id);

    List<HistorySmsSendNotifiResponse> findDetailHistoryAll(UserPrincipal principal, Long id);

    List<HistorySmsSendNotifiResponse> viewContent(UserPrincipal principal, Long id);

    List<ExcelResponse> exportExcelSms(List<Long> idList);
}
