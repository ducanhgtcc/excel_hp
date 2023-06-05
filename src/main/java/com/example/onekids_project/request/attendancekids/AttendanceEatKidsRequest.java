package com.example.onekids_project.request.attendancekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceEatKidsRequest extends IdRequest {
    private boolean breakfast;

    private boolean secondBreakfast;

    private boolean lunch;

    private boolean afternoon;

    private boolean secondAfternoon;

    private boolean dinner;
}
