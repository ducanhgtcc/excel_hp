package com.example.onekids_project.service.servicecustom.chart;

import com.example.onekids_project.request.chart.*;
import com.example.onekids_project.response.chart.*;
import com.example.onekids_project.response.excel.ExcelResponse;

import java.util.List;

/**
 * date 2021-09-07 2:06 PM
 *
 * @author nguyễn văn thụ
 */
public interface ChartKidsService {
    List<Long> findAllStatusKids(Long idSchool);

    List<ChartStatusKidsResponse> findStuddingStatusKids(Long idSchool, StatusKidsChartRequest request);

    List<KidsStatusTimelineResponse> findFiveStatusKids(Long idSchool, int year);
    List<KidsStatusTimelineResponse> findFirstMonthStatusKids(Long idSchool, int year);

    List<EmployeeStatusTimelineResponse> findFourStatusEmployee(Long idSchool, int year);

    List<ChartStatusKidsResponse> findDetailStatusKids(Long idSchool, StatusKidsChartRequest request);

    List<ChartEvaluateResponse> getEvaluateChart(Long idSchool, EvaluateKidsChartRequest request);

    List<ChartAlbumResponse> getAlbumChart(Long idSchool, AlbumKidsChartRequest request);

    List<ChartAlbumResponse> getAlbumSchoolDateChart(Long idSchool, AlbumKidsChartRequest request);

    List<ChartAlbumResponse> getAlbumDateChart(Long idSchool, AlbumKidsChartRequest request);

    List<ChartAttendanceResponse> getAttendanceChart(Long idSchool, AttendanceKidsChartRequest request);

    List<ChartFeesResponse> getFeesKidsChart(Long idSchool, FeesKidsChartRequest request);

    //Chart status excel
    List<ExcelResponse> getExcelStatusKidsForChart(Long idSchool, int year);

    List<ExcelResponse> getExcelStatusEmployeeForChart(Long idSchool, int year);

}
