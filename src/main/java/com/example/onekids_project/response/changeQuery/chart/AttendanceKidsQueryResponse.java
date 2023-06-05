package com.example.onekids_project.response.changeQuery.chart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-10-13 11:36 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@AllArgsConstructor
public class AttendanceKidsQueryResponse {

    private LocalDate attendanceDate;

    private Long idKid;

    private boolean morning;

    private boolean morningYes;

    private boolean morningNo;

    private boolean afternoon;

    private boolean afternoonYes;

    private boolean afternoonNo;

    private boolean evening;

    private boolean eveningYes;

    private boolean eveningNo;
}
