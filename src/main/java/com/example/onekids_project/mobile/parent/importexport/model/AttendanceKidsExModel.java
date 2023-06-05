package com.example.onekids_project.mobile.parent.importexport.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceKidsExModel {

    private Long id;

    private String kidName;

    private String attendanceDate;

    private String absentLetterYes;

    private String absentLetterNo;

    private String absentStatus;

    private String morning;
    private String morningYes;
    private String morningNo;

    private String afternoon;
    private String afternoonYes;
    private String afternoonNo;

    private String evening;
    private String eveningYes;
    private String eveningNo;

    private String eatBreakfast;

    private String eatSecondBreakfast;

    private String eatLunch;

    private String eatAfternoon;

    private String eatSecondAfternoon;

    private String eatDinner;

    private String timeArriveKid;

    private String timeLeaveKid;

    private String minutePickupLate;
}
