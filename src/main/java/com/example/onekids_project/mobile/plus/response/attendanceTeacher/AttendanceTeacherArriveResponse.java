package com.example.onekids_project.mobile.plus.response.attendanceTeacher;

import com.example.onekids_project.mobile.response.AttendanceStatusDayResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-02 8:26 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AttendanceTeacherArriveResponse extends IdResponse {

    private String teacherName;

    private String avatar;

    private String content;

    private String picture;

    private String time;

    private boolean status;

    private AttendanceStatusDayResponse attendanceMorning;

    private AttendanceStatusDayResponse attendanceAfternoon;

    private AttendanceStatusDayResponse attendanceEvening;
}
