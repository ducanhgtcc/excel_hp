package com.example.onekids_project.response.attendanceemployee;

import lombok.Data;

/**
 * date 2021-03-10 9:16 SA
 *
 * @author ADMIN
 */

@Data
public class AttendanceDetailDayEmployeeResponse {

    private AttendanceInfoEmployeeResponse infoEmployeeResponse;

    private AttendanceDetailResponse attendanceDetail;

    private boolean isArrive;

}
