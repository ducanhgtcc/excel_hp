package com.example.onekids_project.request.attendanceemployee;

import lombok.Data;

/**
 * date 2021-03-12 17:03
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEmployeeConfigResponse {

    private boolean morning;

    private boolean afternoon;

    private boolean evening;

    private boolean breakfast;

    private boolean lunch;

    private boolean dinner;
}
