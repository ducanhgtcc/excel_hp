package com.example.onekids_project.service.servicecustom.cashinternal;

import com.example.onekids_project.request.cashinternal.*;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.response.caskinternal.CashInternalinResponse;
import com.example.onekids_project.response.caskinternal.ListCashInternalinResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.itextpdf.text.DocumentException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CashInternalService {

    ListCashInternalinResponse searchListCashOut(UserPrincipal principal, SeacrhListpayRequest request);

    double getCashOutTotal(UserPrincipal principal, SearchPayDateMonth request);

    boolean createCashInternal(UserPrincipal principal, CreateCashInternalRequest request) throws FirebaseMessagingException;

    boolean updateManyApproved(UserPrincipal principal, StatusListRequest request);

    boolean cancelMany(UserPrincipal principal, StatusListRequest request);

    int updateManyUnApproved(UserPrincipal principal, List<UpdateApproveCashRequest> request, Long id);

    int updateManyCancel(UserPrincipal principal, List<UpdateActiveCashRequest> request, Long id);

    boolean approveCash(UserPrincipal principal, Long id, boolean status);

    CashInternalinResponse findDeTailCashInternal(UserPrincipal principal, Long id);

    boolean updateCashInternal(UserPrincipal principal, UpdateCashinternalInRequest request);

    ListCashInternalinResponse searchListCashIn(UserPrincipal principal, SeacrhListpayRequest request);

    boolean createCashCollect(UserPrincipal principal, CreateCashInternalRequest request) throws IOException, DocumentException, FirebaseMessagingException;

    boolean paymentCash(UserPrincipal principal, Long id);

    ResponseEntity<Resource> printpdf(UserPrincipal principal, Long id) throws IOException, DocumentException;
}
