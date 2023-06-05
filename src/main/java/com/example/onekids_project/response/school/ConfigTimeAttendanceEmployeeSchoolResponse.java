package com.example.onekids_project.response.school;

import lombok.Data;

import java.time.LocalTime;

/**
 * date 2021-03-10 4:27 CH
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class ConfigTimeAttendanceEmployeeSchoolResponse {

    private LocalTime timeMorningEmployee;

    private LocalTime timeAfternoonEmployee;

    private LocalTime timeEveningEmployee;

    private LocalTime timeLeaveMorningEmployee;

    private LocalTime timeLeaveAfternoonEmployee;

    private LocalTime timeLeaveEveningEmployee;
}
