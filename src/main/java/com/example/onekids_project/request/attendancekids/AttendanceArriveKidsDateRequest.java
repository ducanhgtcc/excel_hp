package com.example.onekids_project.request.attendancekids;

import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.request.classes.MaClassOtherRequest;
import com.example.onekids_project.request.kids.KidOtherRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AttendanceArriveKidsDateRequest extends IdRequest {
    @NotNull
    @Valid
    private MaClassOtherRequest maClass;

    @NotNull
    @Valid
    private KidOtherRequest kids;

    @Valid
    @NotNull
    private AttendanceArriveKidsRequest attendanceArriveKids;

    @Override
    public String toString() {
        return "AttendanceArriveKidsDateRequest{" +
                "maClass=" + maClass +
                ", kids=" + kids +
                ", attendanceArriveKids=" + attendanceArriveKids +
                "} " + super.toString();
    }
}
