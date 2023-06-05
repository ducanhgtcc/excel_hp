package com.example.onekids_project.response.attendancekids;

import com.example.onekids_project.dto.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceEatKidsResponse extends BaseDTO<String> {
    private boolean breakfast;

    private boolean secondBreakfast;

    private boolean lunch;

    private boolean afternoon;

    private boolean secondAfternoon;

    private boolean dinner;
}
