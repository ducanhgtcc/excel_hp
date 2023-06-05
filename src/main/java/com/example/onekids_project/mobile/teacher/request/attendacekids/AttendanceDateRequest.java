package com.example.onekids_project.mobile.teacher.request.attendacekids;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class AttendanceDateRequest {
    @NotNull
    private  LocalDate localDate;
}
