package com.example.onekids_project.mobile.teacher.response.evaluate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalTeacherResponse {
    //lấy trong 1 tháng
    private int dateNumber;

    //lấy hết
    private int weekNumber;

    //lấy hết
    private int monthNumber;

    //lấy hết
    private int periodicNumber;
}
