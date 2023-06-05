package com.example.onekids_project.mobile.teacher.response.attendancekids;

import com.example.onekids_project.mobile.response.AttendanceStatusDayResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceKidsArriveTeacherResponse extends IdResponse {
    private Long idKid;

    private String kidName;

    private String nickName;

    private String avatar;

    private String content;

    private String picture;

    private String time;

    private boolean status;

    private AttendanceStatusDayResponse morningList;
    private AttendanceStatusDayResponse afternoonList;
    private AttendanceStatusDayResponse eveningList;

}
