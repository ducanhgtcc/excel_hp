package com.example.onekids_project.mobile.plus.response.attendanceTeacher;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * date 2021-06-03 2:45 PM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
public class AttendanceTeacherLeaveResponse extends IdResponse {

    private String teacherName;

    private String avatar;

    private String content;

    private String picture;

    private String time;

    private boolean status;

    private String statusArrive;
}
