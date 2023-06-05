package com.example.onekids_project.importexport.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * date 2021-03-30 09:53
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEmployeeDetailMonth {

    private LocalDate date;

    private boolean morning;

    private boolean morningYes;

    private boolean morningNo;

    private boolean afternoon;

    private boolean afternoonYes;

    private boolean afternoonNo;

    private boolean evening;

    private boolean eveningYes;

    private boolean eveningNo;

    private LocalTime arriveTime;

    private int minuteArriveLate;

    private int minuteLeaveSoon;

    private LocalTime leaveTime;

    private boolean breakfast;

    private boolean lunch;

    private boolean dinner;

    private boolean goSchool;

    private boolean absentYes;

    private boolean absentNo;
}
