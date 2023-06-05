package com.example.onekids_project.mobile.plus.response.attendanceTeacher;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-06-04 10:20 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AttendanceTeacherEatResponse extends IdResponse {

    private String teacherName;

    private String avatar;

    private boolean status;

    private String statusArrive;

    private Boolean breakfast;

    private Boolean lunch;

    private Boolean dinner;
}
