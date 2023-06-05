package com.example.onekids_project.response.attendancekids;

import com.example.onekids_project.dto.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class AttendanceArriveKidsResponse extends BaseDTO<String> {
    private boolean morning;

    private boolean afternoon;

    private boolean evening;

    private String arriveContent;

    private String arriveUrlPicture;

    private LocalTime timeArriveKid;

}
