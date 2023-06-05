package com.example.onekids_project.mobile.plus.response.home;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsStatisticalResponse {
    //tổng số học sinh
    private int kidTotal;

    //số học sinh đang học
    private int kidStudy;

    //đã điểm danh
    private int kidAttendanceYes;

    //chưa điểm danh
    private int kidAttendanceNo;

    //đi học
    private int kidGoSchool;

    //đã điểm danh về
    private int kidLeaveSchool;
}
