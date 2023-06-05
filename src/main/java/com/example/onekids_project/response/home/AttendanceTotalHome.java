package com.example.onekids_project.response.home;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceTotalHome {
    //đã điểm danh
    private int attendaceYes;

    //chưa điểm danh
    private int attendaceNo;

    private int attendanceEatYes;

    private int attendanceEatNo;
}
