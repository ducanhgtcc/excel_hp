package com.example.onekids_project.importexport.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * date 2021-03-27 10:26
 *
 * @author Phạm Ngọc Thắng
 */

@Getter
@Setter
public class AttendanceEmployeeDate {

    private String name;

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
