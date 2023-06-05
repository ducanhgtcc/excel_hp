package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.common.AttendanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AttendanceSampleCreateRequest {
    @NotBlank
    @StringInList(values = {AttendanceConstant.ATTENDANCE_TYPE_ARRIVE, AttendanceConstant.ATTENDANCE_TYPE_LEAVE})
    private String attendanceType;

    @NotBlank
    private String attendanceContent;
}
