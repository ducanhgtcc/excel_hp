package com.example.onekids_project.mobile.parent.response.attendance;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AttendanceEatResponse {
    private int all;

    private int morning;

    private int secondMorning;

    private int lunch;

    private int afternoon;

    private int secondAfternoon;

    private int evening;

    private List<EatDateResponse> eatDateList;

}
