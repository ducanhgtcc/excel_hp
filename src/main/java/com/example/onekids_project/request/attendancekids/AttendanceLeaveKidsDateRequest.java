package com.example.onekids_project.request.attendancekids;

import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.request.classes.MaClassOtherRequest;
import com.example.onekids_project.request.kids.KidOtherRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AttendanceLeaveKidsDateRequest extends IdRequest {

//    @NotBlank
//    @StringInList(values = {AttendanceConstant.GO_SCHOOL})
//    private String absentStatus;

    @NotNull
    @Valid
    private MaClassOtherRequest maClass;

    @NotNull
    @Valid
    private KidOtherRequest kids;

    @Valid
    @NotNull
    private AttendanceLeaveKidsRequest attendanceLeaveKids;
}
