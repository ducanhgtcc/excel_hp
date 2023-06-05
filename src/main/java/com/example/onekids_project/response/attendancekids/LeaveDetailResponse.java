package com.example.onekids_project.response.attendancekids;

import com.example.onekids_project.dto.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class LeaveDetailResponse extends BaseDTO<String> {

    private String leaveContent;

    private String leaveUrlPicture;

    private LocalTime timeLeaveKid;

    private int minutePickupLate;

}