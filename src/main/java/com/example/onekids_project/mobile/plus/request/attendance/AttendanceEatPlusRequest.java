package com.example.onekids_project.mobile.plus.request.attendance;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AttendanceEatPlusRequest {
    @NotNull
    List<Long> idList;

    private boolean morning;

    private boolean secondMorning;

    private boolean lunch;

    private boolean afternoon;

    private boolean secondAfternoon;

    private boolean dinner;
}
