package com.example.onekids_project.mobile.teacher.response.attendancekids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceKidsLeaveTeacherResponse extends IdResponse {
    private Long idKid;

    private String kidName;

    private String nickName;

    private String avatar;

    private String content;

    private String picture;

    private String time;

    private boolean statusLeave;

    private String statusArrive;
}
