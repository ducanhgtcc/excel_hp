package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class AttendanceSampleConfigResponse extends IdResponse {
    private String attendanceType;

    private String attendanceContent;

    private Long idSchool;
}
