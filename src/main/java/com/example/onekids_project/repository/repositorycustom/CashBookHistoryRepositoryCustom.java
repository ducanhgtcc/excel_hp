package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.request.cashbook.SearchCashBookHistoryRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-01 10:29
 *
 * @author lavanviet
 */
public interface CashBookHistoryRepositoryCustom {
    List<CashBookHistory> searchCashBookHistoryPaging(int year, SearchCashBookHistoryRequest request);

    List<CashBookHistory> searchCashBookHistory(int year, SearchCashBookHistoryRequest request);

    List<CashBookHistory> findCashBookHistoryBefore(int year, SearchCashBookHistoryRequest request );

    List<CashBookHistory> findCashBookHistoryPlus(long idCashBook, LocalDate startDate, LocalDate endDate, String type);

    List<CashBookHistory> findCashBookHistoryBeforePlus(Long idCashBook, LocalDate startDate, String type);

    List<CashBookHistory> findCashBookHistoryChart(Long idCashbook, int year);
}
