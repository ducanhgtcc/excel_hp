package com.example.onekids_project.response.home;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticalHomeResponse {

    private KidsTotalHome kidsTotal;

    private AttendanceTotalHome attendanceTotal;

    private LeaveTotalHome leaveTotal;

    private OtherTotalHome otherTotal;

}
