package com.example.onekids_project.mobile.parent.response.attendance;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class AttendanceMobileExcelResponse {

    private Long id;


    private LocalDate date;

    private boolean absentStatus;

    private boolean absentLetterYes;

    private boolean absentLetterNo;


    private List<Boolean> morningList;
    private List<Boolean> afternoonList;
    private List<Boolean> eveningList;



    //sang, phu sang, trua, chieu, phu chieu, toi
    private List<Boolean> eatList;

    private LocalTime timeArriveKid;

    private String arriveLink;

    private String arriveContent;

    private LocalTime timeLeaveKid;

    private String leaveLink;

    private String leaveContent;

    private int minutePickupLate;

    public AttendanceMobileExcelResponse() {

        this.eatList = new ArrayList<>();
        this.morningList = new ArrayList<>();
        this.afternoonList = new ArrayList<>();
        this.eveningList = new ArrayList<>();
    }
}
