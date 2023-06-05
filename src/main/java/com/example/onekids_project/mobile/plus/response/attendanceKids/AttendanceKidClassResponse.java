package com.example.onekids_project.mobile.plus.response.attendanceKids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceKidClassResponse extends IdResponse {

    private String className;

    private AttendanceKidResponse attendanceKidResponse;
}
