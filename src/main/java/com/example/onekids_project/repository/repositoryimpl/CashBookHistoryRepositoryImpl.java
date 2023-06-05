package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.repository.repositorycustom.CashBookHistoryRepositoryCustom;
import com.example.onekids_project.request.cashbook.SearchCashBookHistoryRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-03-01 10:29
 *
 * @author lavanviet
 */
public class CashBookHistoryRepositoryImpl extends BaseRepositoryimpl<CashBookHistory> implements CashBookHistoryRepositoryCustom {
    @Override
    public List<CashBookHistory> searchCashBookHistoryPaging(int year, SearchCashBookHistoryRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setCashBookHistory(queryStr, mapParams, year, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<CashBookHistory> searchCashBookHistory(int year, SearchCashBookHistoryRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setCashBookHistory(queryStr, mapParams, year, request);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }


    @Override
    public List<CashBookHistory> findCashBookHistoryBefore(int year, SearchCashBookHistoryRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_cash_book=:idCashBook ");
        mapParams.put("idCashBook", request.getIdCashBook());
        if (StringUtils.isNotBlank(request.getTypeCashbook())) {
            queryStr.append("and type=:type ");
            mapParams.put("type", request.getTypeCashbook());
        }
        List<CashBookHistory> cashBookHistoryList = new ArrayList<>();
        LocalDate date;
        String type = request.getType();
        if (FinanceConstant.TYPE_MONTH.equals(type) && request.getMonth() != null) {
            date = LocalDate.of(year, request.getMonth(), 1);
        } else if (FinanceConstant.TYPE_DATE.equals(type) && CollectionUtils.isNotEmpty(request.getDateList())) {
            date = request.getDateList().get(0);
        } else {
            return cashBookHistoryList;
        }
        LocalDate startDate = LocalDate.of(year, 1, 1);
        queryStr.append("and date>=:startDate and date<:date ");
        mapParams.put("startDate", startDate);
        mapParams.put("date", date);
        cashBookHistoryList = findAllNoPaging(queryStr.toString(), mapParams);
        return cashBookHistoryList;
    }

    @Override
    public List<CashBookHistory> findCashBookHistoryPlus(long idCashBook, LocalDate startDate, LocalDate endDate, String type) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_cash_book=:idCashBook ");
        mapParams.put("idCashBook", idCashBook);
        queryStr.append("and date>=:startDate and date<=:endDate ");
        mapParams.put("startDate", startDate);
        mapParams.put("endDate", endDate);
        if (StringUtils.isNotBlank(type)) {
            queryStr.append("and type=:type ");
            mapParams.put("type", type);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<CashBookHistory> findCashBookHistoryBeforePlus(Long idCashBook, LocalDate startDate, String type) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_cash_book=:idCashBook ");
        mapParams.put("idCashBook", idCashBook);
        queryStr.append("and date>=:startDate and date<:date ");
        mapParams.put("startDate", LocalDate.of(startDate.getYear(), 1, 1));
        mapParams.put("date", startDate);
        if (StringUtils.isNotBlank(type)) {
            queryStr.append("and type=:type ");
            mapParams.put("type", type);
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<CashBookHistory> findCashBookHistoryChart(Long idCashbook, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_cash_book=:idCashBook ");
        mapParams.put("idCashBook", idCashbook);
        queryStr.append("and year(date)=:year ");
        mapParams.put("year", year);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void setCashBookHistory(StringBuilder queryStr, Map<String, Object> mapParams, int year, SearchCashBookHistoryRequest request) {
        queryStr.append("and id_cash_book=:idCashBook ");
        mapParams.put("idCashBook", request.getIdCashBook());
        LocalDate startDate;
        LocalDate endDate;
        String type = request.getType();
        if (FinanceConstant.TYPE_MONTH.equals(type) && request.getMonth() != null) {
            startDate = LocalDate.of(year, request.getMonth(), 1);
            endDate = startDate.plusMonths(1).minusDays(1);
        } else if (FinanceConstant.TYPE_DATE.equals(type) && CollectionUtils.isNotEmpty(request.getDateList())) {
            startDate = request.getDateList().get(0);
            endDate = request.getDateList().get(1);
        } else {
            startDate = LocalDate.of(year, 1, 1);
            endDate = startDate.plusYears(1).minusDays(1);
        }
        if (StringUtils.isNotBlank(request.getTypeCashbook())) {
            queryStr.append("and type=:type ");
            mapParams.put("type", request.getTypeCashbook());
        }
        queryStr.append("and date>=:startDate and date<=:endDate ");
        mapParams.put("startDate", startDate);
        mapParams.put("endDate", endDate);
        queryStr.append("order by id desc");
    }

}
