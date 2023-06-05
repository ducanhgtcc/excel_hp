package com.example.onekids_project.response.attendanceemployee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;

/**
 * date 2021-03-08 2:05 CH
 *
 * @author ADMIN
 */
@Data
public class AttendanceConfigEmployeeResponse extends IdResponse {

    private String fullName;

    private String phone;

    AttendanceConfigSampleResponse attendanceConfig;
}
