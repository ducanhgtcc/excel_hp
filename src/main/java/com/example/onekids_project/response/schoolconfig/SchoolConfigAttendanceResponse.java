package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class SchoolConfigAttendanceResponse extends IdResponse {

    private LocalTime timePickupKid;
}
