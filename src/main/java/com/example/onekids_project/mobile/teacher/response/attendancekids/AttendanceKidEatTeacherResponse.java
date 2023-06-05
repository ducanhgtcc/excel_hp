package com.example.onekids_project.mobile.teacher.response.attendancekids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;

@Data
public class AttendanceKidEatTeacherResponse extends IdResponse {

    private String kidName;

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
