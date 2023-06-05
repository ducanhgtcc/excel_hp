package com.example.onekids_project.mobile.plus.response.attendanceTeacher;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-01 9:30 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AttendanceDayTeacherPlusResponse {
    private int sumTeacher;

    private int sumTeacherOff;

    private int sumTeacherAttendance;

    private int sumTeacherNoAttendance;
}
