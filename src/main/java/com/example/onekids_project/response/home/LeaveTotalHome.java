package com.example.onekids_project.response.home;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveTotalHome {
    //số học sinh đi học
    private int goSchool;

    //đã về
    private int leaveYes;

    //chưa về
    private int leaveNo;

    //số học sinh đón muộn
    private int pickupLater;
}
