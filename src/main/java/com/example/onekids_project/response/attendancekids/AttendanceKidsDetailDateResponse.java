package com.example.onekids_project.response.attendancekids;

import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.entity.kids.AttendanceEatKids;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceKidsDetailDateResponse extends IdResponse {

    private LocalDate attendanceDate;

    //true là nghỉ có phép hoặc không phép cả ngày đó
//    private boolean absentStatus;

    private boolean attendanceArrive;

    private MaClassOtherResponse maClass;

    private KidOtherResponse kids;

    private ArriveDetailResponse attendanceArriveKids;

    private LeaveDetailResponse attendanceLeaveKids;

    private AttendanceEatKidsResponse attendanceEatKids;
}
