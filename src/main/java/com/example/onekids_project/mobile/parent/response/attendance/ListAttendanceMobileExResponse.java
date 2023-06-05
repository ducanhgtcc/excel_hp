package com.example.onekids_project.mobile.parent.response.attendance;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListAttendanceMobileExResponse {

    private boolean lastPage;

    private List<AttendanceMobileExcelResponse> attendanceMobileExcelResponseList;
}
