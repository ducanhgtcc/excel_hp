package com.example.onekids_project.response.attendancekids;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceArriveStatusResponse {
    private boolean morning;

    private boolean morningYes;

    private boolean morningNo;

    private boolean afternoon;

    private boolean afternoonYes;

    private boolean afternoonNo;

    private boolean evening;

    private boolean eveningYes;

    private boolean eveningNo;
}
