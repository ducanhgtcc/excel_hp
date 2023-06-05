package com.example.onekids_project.service.servicecustom.cashbook;

import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.request.cashbook.SearchCashBookHistoryRequest;
import com.example.onekids_project.response.cashbook.CashbookMoneyResponse;
import com.example.onekids_project.response.cashbook.ListCashBookHistoryResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;

/**
 * date 2021-03-01 10:32
 *
 * @author lavanviet
 */
public interface CashBookHistoryService {
    CashBookHistory saveCashBookHistory(Long idSchool, String category, String type, LocalDate date, double money, String origin, String identify);

    void updateCashBookHistory(CashBookHistory cashBookHistory);

    ListCashBookHistoryResponse searchCashBookHistory(UserPrincipal principal, SearchCashBookHistoryRequest request);

    ListCashBookHistoryResponse searchCashBookHistoryNow(UserPrincipal principal, SearchCashBookHistoryRequest request);
    CashbookMoneyResponse searchCashBookHistoryYear(UserPrincipal principal, Integer year, SearchCashBookHistoryRequest request);

}
