package com.example.onekids_project.mobile.plus.response.attendanceKids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceArriveKidClassResponse extends IdResponse {
    // id điểm danh đến
    private Long idKid;

    private String nameKid;

    private String nickName;

    private String avatar;

    private String content;

    private String picture;

    private String time;

    private boolean status;

    private AttendanceStatusDayPlusResponse morning;

    private AttendanceStatusDayPlusResponse affternoon;

    private AttendanceStatusDayPlusResponse evening;

}
