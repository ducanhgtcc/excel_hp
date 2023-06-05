package com.example.onekids_project.mobile.plus.response.attendanceKids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceEatKidClassResponse extends IdResponse {

    private String nameKid;

    private String nickName;

    private String avatar;

    private boolean statusEat;

    private String statusArrive;

    private boolean morning;

    private boolean secondMorning;

    private boolean lunch;

    private boolean afternoon;

    private boolean secondAfternoon;

    private boolean dinner;
}
