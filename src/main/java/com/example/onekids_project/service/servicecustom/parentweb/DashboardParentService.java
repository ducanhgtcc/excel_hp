package com.example.onekids_project.service.servicecustom.parentweb;

import com.example.onekids_project.response.chart.ChartAttendanceResponse;
import com.example.onekids_project.response.parentweb.ChangeKidsResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author lavanviet
 */
public interface DashboardParentService {
    List<ChartAttendanceResponse> getAttendanceChart();

    List<ChangeKidsResponse> getKidsOfParent();

    ResponseEntity changeKidsOfParent(Long idKid);
}
