package com.example.onekids_project.response.attendancekids;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAttendanceKidsDetailDateResponse {
    private AttendanceConfigResponse attendanceConfigResponse;

    private List<AttendanceKidsDetailDateResponse> attendanceDetailDateList;
}
