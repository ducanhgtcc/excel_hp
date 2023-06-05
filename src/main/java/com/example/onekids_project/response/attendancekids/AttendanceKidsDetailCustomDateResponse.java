package com.example.onekids_project.response.attendancekids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceKidsDetailCustomDateResponse extends IdResponse {

    private LocalDate attendanceDate;

    private boolean attendanceArrive;

    private MaClassOtherResponse maClass;

    private KidOtherResponse kids;

    private ArriveDetailResponse attendanceArriveKids;

    private LeaveDetailResponse attendanceLeaveKids;

    private AttendanceEatKidsResponse attendanceEatKids;
}
