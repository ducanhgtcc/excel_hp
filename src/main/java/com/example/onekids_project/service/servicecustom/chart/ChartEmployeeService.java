package com.example.onekids_project.service.servicecustom.chart;

import com.example.onekids_project.request.chart.AttendanceEmployeeChartRequest;
import com.example.onekids_project.request.chart.FeesKidsChartRequest;
import com.example.onekids_project.request.chart.StatusEmployeeChartRequest;
import com.example.onekids_project.response.chart.ChartAttendanceResponse;
import com.example.onekids_project.response.chart.ChartFeesResponse;
import com.example.onekids_project.response.chart.ChartStatusEmployeeResponse;
import com.example.onekids_project.response.chart.ListChartStatusEmployeeResponse;

import java.util.List;

/**
 * date 2021-09-29 11:20 AM
 *
 * @author nguyễn văn thụ
 */
public interface ChartEmployeeService {

    ListChartStatusEmployeeResponse findAllStatusEmployee(Long idSchool);

    List<ChartStatusEmployeeResponse> findDetailStatusEmployee(Long idSchool, StatusEmployeeChartRequest request);

    List<ChartAttendanceResponse> getAttendanceChart(Long idSchool, AttendanceEmployeeChartRequest request);

    List<ChartFeesResponse> getFinanceEmployeeChart(Long idSchool, FeesKidsChartRequest request);

}
