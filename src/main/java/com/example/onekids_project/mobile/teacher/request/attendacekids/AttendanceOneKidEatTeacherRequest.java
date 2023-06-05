package com.example.onekids_project.mobile.teacher.request.attendacekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceOneKidEatTeacherRequest extends IdRequest {
    private boolean morning;
    private boolean secondMorning;
    private boolean lunch;
    private boolean afternoon;
    private boolean secondAfternoon;
    private boolean dinner;

    @Override
    public String toString() {
        return "AttendanceOneKidEatTeacherRequest{" +
                "morning=" + morning +
                ", secondMorning=" + secondMorning +
                ", lunch=" + lunch +
                ", afternoon=" + afternoon +
                ", secondAfternoon=" + secondAfternoon +
                ", dinner=" + dinner +
                '}';
    }
}
