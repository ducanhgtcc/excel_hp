package com.example.onekids_project.response.home;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticalClassHomeResponse {
    private String className;

    private String gradeName;

    private int kidsTotalNumber;

    private int attendanceYes;

    private int attendanceNo;

    private int goSchoolNumber;

    private int absentNumber;

    private int leaveYes;

    private int leaveNo;
}
