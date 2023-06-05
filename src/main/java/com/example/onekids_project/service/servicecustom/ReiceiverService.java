package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.dto.ReiceiversDTO;
import com.example.onekids_project.request.AppSend.ReceiversRequest;
import com.example.onekids_project.request.notifihistory.UpdateSmsAppReiceiverAprovedRequest;
import com.example.onekids_project.request.notifihistory.UpdateSmsAppReiceiverRevokeRequest;
import com.example.onekids_project.response.appsend.ReceiversResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;
import java.util.Optional;

public interface ReiceiverService {

    Optional<ReiceiversDTO> findByidReiceivers(Long idSchoolLogin, Long id);

    ReceiversResponse updateRevoke1(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppReiceiverRevokeRequest updateSmsAppReiceiverRevokeRequest);

    ReceiversResponse updateApprove(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppReiceiverAprovedRequest updateSmsAppReiceiverAprovedRequest);

    boolean deleteReiceivers(Long idSchoolLogin, Long id);

    ReceiversResponse updateRevoke2(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppReiceiverRevokeRequest updateSmsAppReiceiverRevokeRequest);

    ReceiversRequest updateManyApprove(Long id, List<ReceiversRequest> receiversRequests);
}
