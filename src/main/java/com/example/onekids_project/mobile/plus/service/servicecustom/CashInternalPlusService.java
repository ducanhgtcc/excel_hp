package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.cashinternal.CashBookPlusRequest;
import com.example.onekids_project.mobile.plus.request.cashinternal.CashInternalPlusRequest;
import com.example.onekids_project.mobile.plus.response.cashinternal.CashBookPlusResponse;
import com.example.onekids_project.mobile.plus.response.cashinternal.NumberCashInternalResponse;
import com.example.onekids_project.mobile.request.IdAndStatusRequest;
import com.example.onekids_project.mobile.response.ListCashInternalPlusResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-06-18 14:15
 *
 * @author lavanviet
 */
public interface CashInternalPlusService {
    ListCashInternalPlusResponse getCashInternalPlus(UserPrincipal principal, CashInternalPlusRequest request, String category);
    boolean approvedCashInternalPlus(UserPrincipal principal, List<IdAndStatusRequest> request);
    boolean canceledCashInternalPlus(UserPrincipal principal, Long id);
    List<CashBookPlusResponse> getCashBookPlus(UserPrincipal principal, CashBookPlusRequest request);

    NumberCashInternalResponse getShowNumber(UserPrincipal principal);
}
