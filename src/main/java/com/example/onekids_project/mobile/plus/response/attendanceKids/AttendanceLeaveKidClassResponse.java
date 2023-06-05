package com.example.onekids_project.mobile.plus.response.attendanceKids;

import com.example.onekids_project.mobile.response.AttendanceStatusDayResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceLeaveKidClassResponse extends IdResponse {

    // id điểm danh về
    private Long idKid;

    private String nameKid;

    private String nickName;

    private String avatar;

    private String content;

    private String picture;

    private String time;

    // trạng thái điểm danh
    private boolean statusLeave;

    // trạng thái đi học-> NOARRIVE, ARRIVE, OFF (chưa điểm danh đến, đi học, nghỉ học)
    private String statusArrive;

}
