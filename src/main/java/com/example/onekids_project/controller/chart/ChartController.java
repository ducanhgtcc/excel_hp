package com.example.onekids_project.controller.chart;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.request.chart.*;
import com.example.onekids_project.response.chart.*;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.chart.ChartCashInternalService;
import com.example.onekids_project.service.servicecustom.chart.ChartEmployeeService;
import com.example.onekids_project.service.servicecustom.chart.ChartKidsService;
import com.example.onekids_project.util.RequestUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-09-07 11:21 AM
 *
 * @author nguyễn văn thụ
 */
@RestController
@RequestMapping("/web/chart/statistical")
public class ChartController {

    @Autowired
    private ChartKidsService chartKidsService;
    @Autowired
    private ChartEmployeeService chartEmployeeService;
    @Autowired
    private ChartCashInternalService chartCashInternalService;

    @RequestMapping(method = RequestMethod.GET, value = "/chart-status")
    public ResponseEntity getAllKidsSchool(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequestPlus(principal);
        List<Long> data = chartKidsService.findAllStatusKids(principal.getIdSchoolLogin());
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/chart-status/studding")
    public ResponseEntity getStuddingKidsSchool(@CurrentUser UserPrincipal principal, @Valid StatusKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartStatusKidsResponse> data = chartKidsService.findStuddingStatusKids(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/chart-status/five-status")
    public ResponseEntity getFiveStatusKidsSchool(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequestPlus(principal, year);
        List<KidsStatusTimelineResponse> data = chartKidsService.findFiveStatusKids(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/chart-status/fist-month")
    public ResponseEntity getFistMonthStatusKidsSchool(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequestPlus(principal, year);
        List<KidsStatusTimelineResponse> data = chartKidsService.findFirstMonthStatusKids(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/chart-status/detail")
    public ResponseEntity getDetailKidsSchool(@CurrentUser UserPrincipal principal, @Valid StatusKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartStatusKidsResponse> data = chartKidsService.findDetailStatusKids(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Xuất excel trạng thái học sinh theo năm
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "chart-status/excel")
    public ResponseEntity excelStatusKids(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequest(principal, year);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = chartKidsService.getExcelStatusKidsForChart(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/attendance")
    public ResponseEntity getAttendanceKidsSchool(@CurrentUser UserPrincipal principal, @Valid AttendanceKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartAttendanceResponse> data = chartKidsService.getAttendanceChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/evaluate")
    public ResponseEntity getEvaluateKidsSchool(@CurrentUser UserPrincipal principal, @Valid EvaluateKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartEvaluateResponse> data = chartKidsService.getEvaluateChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/album")
    public ResponseEntity getAlbumKidsSchool(@CurrentUser UserPrincipal principal, @Valid AlbumKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartAlbumResponse> data = chartKidsService.getAlbumChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/album/school")
    public ResponseEntity getAlbumAllKidsSchool(@CurrentUser UserPrincipal principal, @Valid AlbumKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartAlbumResponse> data = chartKidsService.getAlbumSchoolDateChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/album/date")
    public ResponseEntity getAlbumDateKidsSchool(@CurrentUser UserPrincipal principal, @Valid AlbumKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartAlbumResponse> data = chartKidsService.getAlbumDateChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/fees")
    public ResponseEntity getFeesKids(@CurrentUser UserPrincipal principal, @Valid FeesKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartFeesResponse> data = chartKidsService.getFeesKidsChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/chart-status-employee")
    public ResponseEntity getAllEmployeeSchool(@CurrentUser UserPrincipal principal, @Valid StatusEmployeeChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        if (AppConstant.CHART_ALL.equals(request.getType())) {
            ListChartStatusEmployeeResponse data = chartEmployeeService.findAllStatusEmployee(principal.getIdSchoolLogin());
            return NewDataResponse.setDataSearch(data);
        } else {
            List<ChartStatusEmployeeResponse> data = chartEmployeeService.findDetailStatusEmployee(principal.getIdSchoolLogin(), request);
            return NewDataResponse.setDataSearch(data);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/chart-status-employee/four-status")
    public ResponseEntity getFourStatusEmployeeSchool(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequestPlus(principal, year);
        List<EmployeeStatusTimelineResponse> data = chartKidsService.findFourStatusEmployee(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(data);
    }

    /**
     * Xuất excel trạng thái nhân sự theo năm
     *
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "chart-status-employee/excel")
    public ResponseEntity excelStatusEmployee(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequest(principal, year);
        CommonValidate.checkPlusOrTeacher(principal);
        List<ExcelResponse> responseList = chartKidsService.getExcelStatusEmployeeForChart(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/attendance-employee")
    public ResponseEntity getAttendanceEmployeeSchool(@CurrentUser UserPrincipal principal, @Valid AttendanceEmployeeChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartAttendanceResponse> data = chartEmployeeService.getAttendanceChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/finace")
    public ResponseEntity getFinaceKids(@CurrentUser UserPrincipal principal, @Valid FeesKidsChartRequest request) {
        RequestUtils.getFirstRequestPlus(principal, request);
        List<ChartFeesResponse> data = chartEmployeeService.getFinanceEmployeeChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cashinternal-paymoney")
    public ResponseEntity getCashInternalPayMoney(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequestPlus(principal, year);
        List<ChartCashInternalResponse> data = chartCashInternalService.getPayMoneyChart(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cashinternal-cashbook")
    public ResponseEntity getCashInternalCashbook(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequestPlus(principal, year);
        List<ChartCashInternalResponse> data = chartCashInternalService.getCashbookChart(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cashinternal-cashbook-history")
    public ResponseEntity getCashInternalCashbookHistory(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequestPlus(principal, year);
        List<ChartCashInternalHistoryResponse> data = chartCashInternalService.getCashbookChartHistory(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wallet")
    public ResponseEntity getWalletParent(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequestPlus(principal, year);
        List<ChartFeesResponse> data = chartCashInternalService.getWalletParent(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wallet-status")
    public ResponseEntity getWalletParentStatus(@CurrentUser UserPrincipal principal, @Valid int year) {
        RequestUtils.getFirstRequestPlus(principal, year);
        List<ChartFeesResponse> data = chartCashInternalService.getWalletParentStatus(principal.getIdSchoolLogin(), year);
        return NewDataResponse.setDataSearch(data);
    }
}
