package com.example.onekids_project.mobile.plus.request.attendance;

import lombok.Data;

@Data
public class AttendanceStatusDayPlusRequest {
    private boolean goSchool;
    private boolean absentYes;
    private boolean absentNo;
}
