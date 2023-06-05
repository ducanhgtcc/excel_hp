package com.example.onekids_project.mobile.teacher.request.attendacekids;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AttendanceKidEatTeacherRequest {
    @NotNull
    List<Long> idEat;
    private boolean morning;
    private boolean secondMorning;
    private boolean lunch;
    private boolean afternoon;
    private boolean secondAfternoon;
    private boolean dinner;
}
