package com.example.onekids_project.parentcontroller;

import com.example.onekids_project.request.chart.FeesKidsChartRequest;
import com.example.onekids_project.response.chart.ChartAttendanceResponse;
import com.example.onekids_project.response.chart.ChartFeesResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.parentweb.ChangeKidsResponse;
import com.example.onekids_project.response.schoolconfig.SchoolDataResponse;
import com.example.onekids_project.security.model.CurrentUser;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.service.servicecustom.chart.ChartKidsService;
import com.example.onekids_project.service.servicecustom.parentweb.DashboardParentService;
import com.example.onekids_project.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * @author lavanviet
 */
@RestController
@RequestMapping("web/parent/dashboard")
public class DashboardParentController {

    @Autowired
    private DashboardParentService dashboardParentService;
    @Autowired
    private ChartKidsService chartKidsService;
    @Autowired
    private SchoolService schoolService;

    @RequestMapping(method = RequestMethod.GET, value = "/chart/attendance")
    public ResponseEntity searchEvaluateKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<ChartAttendanceResponse> data = dashboardParentService.getAttendanceChart();
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/chart/fees")
    public ResponseEntity getFeesKids(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        FeesKidsChartRequest request = new FeesKidsChartRequest();
        request.setYear(LocalDate.now().getYear());
        List<ChartFeesResponse> data = chartKidsService.getFeesKidsChart(principal.getIdSchoolLogin(), request);
        return NewDataResponse.setDataSearch(data);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/infor")
    public ResponseEntity getSchool(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        SchoolDataResponse response = schoolService.getSchoolData(principal);
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/changeKids")
    public ResponseEntity getKidOfParent(@CurrentUser UserPrincipal principal) {
        RequestUtils.getFirstRequest(principal);
        List<ChangeKidsResponse> response = dashboardParentService.getKidsOfParent();
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/changeKids/{id}")
    public ResponseEntity updateChangeSchool(@CurrentUser UserPrincipal principal, @PathVariable Long id) {
        RequestUtils.getFirstRequest(principal, id);
        ResponseEntity response = dashboardParentService.changeKidsOfParent(id);
        return response;
    }
}
