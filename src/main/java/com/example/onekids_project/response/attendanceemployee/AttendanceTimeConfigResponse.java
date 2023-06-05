package com.example.onekids_project.response.attendanceemployee;

import lombok.Data;

import java.time.LocalTime;

/**
 * date 2021-03-11 16:08
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceTimeConfigResponse {

    private LocalTime timeMorningEmployee;

    private LocalTime timeAfternoonEmployee;

    private LocalTime timeEveningEmployee;

    private LocalTime timeLeaveMorningEmployee;

    private LocalTime timeLeaveAfternoonEmployee;

    private LocalTime timeLeaveEveningEmployee;

}
