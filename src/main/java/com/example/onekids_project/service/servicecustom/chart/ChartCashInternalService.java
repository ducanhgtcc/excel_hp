package com.example.onekids_project.service.servicecustom.chart;

import com.example.onekids_project.response.chart.ChartCashInternalHistoryResponse;
import com.example.onekids_project.response.chart.ChartCashInternalResponse;
import com.example.onekids_project.response.chart.ChartFeesResponse;

import java.util.List;

/**
 * date 2021-09-30 9:57 AM
 *
 * @author nguyễn văn thụ
 */
public interface ChartCashInternalService {

    List<ChartCashInternalResponse> getPayMoneyChart(Long idSchool, int year);

    List<ChartCashInternalResponse> getCashbookChart(Long idSchool, int year);

    List<ChartCashInternalHistoryResponse> getCashbookChartHistory(Long idSchool, int year);

    List<ChartFeesResponse> getWalletParent(Long idSchool, int year);

    List<ChartFeesResponse> getWalletParentStatus(Long idSchool, int year);
}
