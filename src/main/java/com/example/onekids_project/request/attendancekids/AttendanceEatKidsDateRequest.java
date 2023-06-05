package com.example.onekids_project.request.attendancekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AttendanceEatKidsDateRequest extends IdRequest {
    @Valid
    @NotNull
    private AttendanceEatKidsRequest attendanceEatKids;
}
