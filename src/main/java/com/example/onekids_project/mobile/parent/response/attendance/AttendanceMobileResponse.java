package com.example.onekids_project.mobile.parent.response.attendance;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AttendanceMobileResponse extends IdResponse {

    private LocalDate date;

    //tru là không có điểm danh ăn và học
    private boolean status;

    //sang, chieu, toi
    private Map<String, String> statusList;

    //sang, phu sang, trua, chieu, phu chieu, toi
    private List<Boolean> eatList;

    private LocalTime timeArriveKid;

    private String arriveLink;

    private String arriveContent;

    private LocalTime timeLeaveKid;

    private String leaveLink;

    private String leaveContent;
}
