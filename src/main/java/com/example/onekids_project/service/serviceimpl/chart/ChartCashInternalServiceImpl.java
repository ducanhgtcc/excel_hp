package com.example.onekids_project.service.serviceimpl.chart;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.entity.school.FnCashBook;
import com.example.onekids_project.repository.CashBookHistoryRepository;
import com.example.onekids_project.repository.FnCashBookRepository;
import com.example.onekids_project.repository.FnCashInternalSchoolRepository;
import com.example.onekids_project.repository.WalletParentHistoryRepository;
import com.example.onekids_project.response.chart.ChartCashInternalHistoryResponse;
import com.example.onekids_project.response.chart.ChartCashInternalResponse;
import com.example.onekids_project.response.chart.ChartFeesResponse;
import com.example.onekids_project.service.servicecustom.chart.ChartCashInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * date 2021-09-30 9:57 AM
 *
 * @author nguyễn văn thụ
 */
@Service
public class ChartCashInternalServiceImpl implements ChartCashInternalService {

    @Autowired
    private FnCashInternalSchoolRepository fnCashInternalSchoolRepository;
    @Autowired
    private FnCashBookRepository fnCashBookRepository;
    @Autowired
    private CashBookHistoryRepository cashBookHistoryRepository;
    @Autowired
    private WalletParentHistoryRepository walletParentHistoryRepository;

    @Override
    public List<ChartCashInternalResponse> getPayMoneyChart(Long idSchool, int year) {
        List<ChartCashInternalResponse> responseList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            ChartCashInternalResponse response = new ChartCashInternalResponse();
            List<FnCashInternal> cashInternalList = fnCashInternalSchoolRepository.searchCashInternalMonth(idSchool, i, year);
            int month = (i > 10) ? i : Integer.parseInt("0" + i);
            response.setName("Tháng " + month);
            double moneyIn = cashInternalList.stream().filter(x -> FinanceConstant.CATEGORY_IN.equals(x.getCategory())).mapToDouble(FnCashInternal::getMoney).sum();
            double moneyOut = cashInternalList.stream().filter(x -> FinanceConstant.CATEGORY_OUT.equals(x.getCategory())).mapToDouble(FnCashInternal::getMoney).sum();
            response.setMoneyIn(moneyIn);
            response.setMoneyOut(moneyOut);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public List<ChartCashInternalResponse> getCashbookChart(Long idSchool, int year) {
        List<ChartCashInternalResponse> responseList = new ArrayList<>();
        Optional<FnCashBook> fnCashBook = fnCashBookRepository.findBySchoolIdAndYearAndDelActiveTrue(idSchool, year);
        if (fnCashBook.isPresent()) {
            List<CashBookHistory> cashBookHistoryList = cashBookHistoryRepository.findCashBookHistoryChart(fnCashBook.get().getId(), year);
            for (int i = 1; i <= 12; i++) {
                ChartCashInternalResponse response = new ChartCashInternalResponse();
                int finalI = i;
                double moneyIn = cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN) && x.getDate().getMonthValue() == finalI).mapToDouble(CashBookHistory::getMoney).sum();
                double moneyOut = cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT) && x.getDate().getMonthValue() == finalI).mapToDouble(CashBookHistory::getMoney).sum();
                int month = (i > 10) ? i : Integer.parseInt("0" + i);
                response.setName("Tháng " + month);
                response.setMoneyIn(moneyIn);
                response.setMoneyOut(moneyOut);
                responseList.add(response);
            }
        }
        return responseList;
    }

    @Override
    public List<ChartCashInternalHistoryResponse> getCashbookChartHistory(Long idSchool, int year) {
        List<ChartCashInternalHistoryResponse> responseList = new ArrayList<>();
        Optional<FnCashBook> fnCashBook = fnCashBookRepository.findBySchoolIdAndYearAndDelActiveTrue(idSchool, year);
        if (fnCashBook.isPresent()) {
            double total = 0;
            List<CashBookHistory> cashBookHistoryList = cashBookHistoryRepository.findCashBookHistoryChart(fnCashBook.get().getId(), year);
            for (int i = 1; i <= 12; i++) {
                ChartCashInternalHistoryResponse response = new ChartCashInternalHistoryResponse();
                int finalI = i;
                double moneyKidIn = cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN) && FinanceConstant.CASH_BOOK_KID.equals(x.getType()) && x.getDate().getMonthValue() == finalI).mapToDouble(CashBookHistory::getMoney).sum();
                double moneyKidOut = cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT) && FinanceConstant.CASH_BOOK_KID.equals(x.getType()) && x.getDate().getMonthValue() == finalI).mapToDouble(CashBookHistory::getMoney).sum();
                double moneyEmpIn = cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN) && FinanceConstant.CASH_BOOK_EMP.equals(x.getType()) && x.getDate().getMonthValue() == finalI).mapToDouble(CashBookHistory::getMoney).sum();
                double moneyEmpOut = cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT) && FinanceConstant.CASH_BOOK_EMP.equals(x.getType()) && x.getDate().getMonthValue() == finalI).mapToDouble(CashBookHistory::getMoney).sum();
                double moneySchIn = cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN) && FinanceConstant.CASH_BOOK_SCH.equals(x.getType()) && x.getDate().getMonthValue() == finalI).mapToDouble(CashBookHistory::getMoney).sum();
                double moneySchOut = cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT) && FinanceConstant.CASH_BOOK_SCH.equals(x.getType()) && x.getDate().getMonthValue() == finalI).mapToDouble(CashBookHistory::getMoney).sum();
                if (i == 1) {
                    total = (moneyKidIn - moneyKidOut) + (moneyEmpIn - moneyEmpOut) + (moneySchIn - moneySchOut) + fnCashBook.get().getMoneyStart();
                } else {
                    total = (moneyKidIn - moneyKidOut) + (moneyEmpIn - moneyEmpOut) + (moneySchIn - moneySchOut) + total;
                }
                int month = (i > 10) ? i : Integer.parseInt("0" + i);
                response.setName("Tháng " + month);
                response.setMoneyKids(moneyKidIn - moneyKidOut);
                response.setMoneyEmployee(moneyEmpIn - moneyEmpOut);
                response.setMoneySchool(moneySchIn - moneySchOut);
                response.setTotal(total);
                responseList.add(response);
            }
        }
        return responseList;
    }

    @Override
    public List<ChartFeesResponse> getWalletParent(Long idSchool, int year) {
        List<ChartFeesResponse> responseList = new ArrayList<>();
        List<WalletParentHistory> walletParentHistoryList = walletParentHistoryRepository.searchWalletParentHistoryChart(idSchool, year, AppConstant.APP_TRUE);
        for (int i = 1; i <= 12; i++) {
            ChartFeesResponse response = new ChartFeesResponse();
            int finalI = i;
            double moneyIn = walletParentHistoryList.stream().filter(x-> FinanceConstant.CATEGORY_IN.equals(x.getCategory()) && x.getDate().getMonthValue() == finalI).mapToDouble(WalletParentHistory::getMoney).sum();
            double moneyOut = walletParentHistoryList.stream().filter(x-> FinanceConstant.CATEGORY_OUT.equals(x.getCategory()) && x.getDate().getMonthValue() == finalI).mapToDouble(WalletParentHistory::getMoney).sum();
            int month = (i > 10) ? i : Integer.parseInt("0" + i);
            response.setName("Tháng " + month);
            response.setMoneyIn((long) moneyIn);
            response.setMoneyOut((long) moneyOut);
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public List<ChartFeesResponse> getWalletParentStatus(Long idSchool, int year) {
        List<ChartFeesResponse> responseList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            ChartFeesResponse response = new ChartFeesResponse();
            long walletYes = walletParentHistoryRepository.countWalletParentHistoryChart(idSchool, i, year, AppConstant.APP_TRUE);
            long walletNo = walletParentHistoryRepository.countWalletParentHistoryChart(idSchool, i, year, AppConstant.APP_FALSE);
            int month = (i > 10) ? i : Integer.parseInt("0" + i);
            response.setName("Tháng " + month);
            response.setFeesYes(walletYes);
            response.setFeesNo(walletNo);
            responseList.add(response);
        }
        return responseList;
    }
}
