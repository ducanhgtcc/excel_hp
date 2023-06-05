package com.example.onekids_project.request.system;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AttendaceConfigRequest {


    private boolean morningSaturday;

    private boolean afternoonSaturday;

    private boolean eveningSaturday;

    private boolean sunday;

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
