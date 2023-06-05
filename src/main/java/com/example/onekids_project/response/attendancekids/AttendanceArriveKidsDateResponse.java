package com.example.onekids_project.response.attendancekids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceArriveKidsDateResponse extends IdResponse {
    private LocalDate attendanceDate;

//    private String absentStatus;
//
//    private boolean absentLetterYes;
//
//    private boolean absentLetterNo;

    private boolean attendanceArrive;

    private MaClassOtherResponse maClass;

    private KidOtherResponse kids;

    private ArriveDetailResponse attendanceArriveKids;
}
