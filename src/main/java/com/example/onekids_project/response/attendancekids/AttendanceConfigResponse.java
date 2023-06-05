package com.example.onekids_project.response.attendancekids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceConfigResponse extends IdResponse {
    private boolean morningAttendanceArrive;

    private boolean afternoonAttendanceArrive;

    private boolean eveningAttendanceArrive;

    private boolean morningEat;

    private boolean secondMorningEat;

    private boolean lunchEat;

    private boolean afternoonEat;

    private boolean secondAfternoonEat;

    private boolean eveningEat;
}
