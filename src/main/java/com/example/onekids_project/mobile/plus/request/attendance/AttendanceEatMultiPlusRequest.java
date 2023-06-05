package com.example.onekids_project.mobile.plus.request.attendance;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceEatMultiPlusRequest extends IdRequest {

    private boolean morning;

    private boolean secondMorning;

    private boolean lunch;

    private boolean afternoon;

    private boolean secondAfternoon;

    private boolean dinner;
}
