package com.example.onekids_project.mobile.parent.response.attendance;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAttendanceMonthResponse {
    private int all;

    private int morning;

    private int afternoon;

    private int evening;

    private List<Integer> noAttendanceList;

    private List<AttendanceMonthResponse> allList;

    private List<AttendanceMonthResponse> morningList;

    private List<AttendanceMonthResponse> afternoonList;

    private List<AttendanceMonthResponse> eveningList;

}
