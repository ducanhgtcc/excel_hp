package com.example.onekids_project.master.service;

import com.example.onekids_project.master.request.SearchHistorySmsRequest;
import com.example.onekids_project.master.response.HistorySmsResponse;
import com.example.onekids_project.master.response.HistorySmsResponseByStatus;

import java.util.List;

public interface HistorySmsService {

    List<HistorySmsResponse> findAllHistorySms(SearchHistorySmsRequest searchHistorySmsRequest);

    List<HistorySmsResponseByStatus> findByHistorySmsById(Long id, String typeSend);
}
