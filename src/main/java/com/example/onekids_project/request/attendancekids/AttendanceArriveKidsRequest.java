package com.example.onekids_project.request.attendancekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AttendanceArriveKidsRequest extends IdRequest {
    private boolean morning;

    private boolean morningYes;

    private boolean morningNo;

    private boolean afternoon;

    private boolean afternoonYes;

    private boolean afternoonNo;

    private boolean evening;

    private boolean eveningYes;

    private boolean eveningNo;

    private LocalTime timeArriveKid;
}
