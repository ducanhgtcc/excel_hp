package com.example.onekids_project.request.attendancekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AttendanceLeaveKidsRequest extends IdRequest {
    private boolean statusLeave;

    private String leaveContent;

    private String leaveUrlPicture;

    private LocalTime timeLeaveKid;

    private int minutePickupLate;
}
