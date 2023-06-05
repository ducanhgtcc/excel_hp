package com.example.onekids_project.request.school;

import lombok.Data;

import java.time.LocalTime;

/**
 * date 2021-03-11 10:02 SA
 *
 * @author Phạm Ngọc Thắng
 */
@Data
public class ConfigTimeAttendanceEmployeeRequest {

    private LocalTime timeMorningEmployee;

    private LocalTime timeAfternoonEmployee;

    private LocalTime timeEveningEmployee;

    private LocalTime timeLeaveMorningEmployee;

    private LocalTime timeLeaveAfternoonEmployee;

    private LocalTime timeLeaveEveningEmployee;
}
