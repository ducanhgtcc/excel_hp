package com.example.onekids_project.service.servicecustom;

//import org.springframework.data.domain.Pageable;

import com.example.onekids_project.dto.SmsSendDTO;
import com.example.onekids_project.request.notifihistory.SearchSmsSendHistoryRequest;
import com.example.onekids_project.response.notifihistory.ListSmsSendHistoryResponse;

import java.util.Optional;

public interface SmsSendHistoryService {

    ListSmsSendHistoryResponse searchSmsSendHistory(Long idSchoolLogin, SearchSmsSendHistoryRequest searchSmsSendHistoryRequest);

    Optional<SmsSendDTO> findByIdSmsSendHistory(Long idSchoolLogin, Long id);
}
