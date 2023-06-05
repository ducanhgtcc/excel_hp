package com.example.onekids_project.response.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-09-15 10:15 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class ChartAttendanceResponse {

    private String name;

    private long attendance;

    private long attendanceYes;

    private long attendanceNo;

    private long attendanceUn;
}
