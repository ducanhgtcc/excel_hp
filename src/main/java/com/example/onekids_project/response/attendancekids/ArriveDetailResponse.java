package com.example.onekids_project.response.attendancekids;

import com.example.onekids_project.dto.base.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ArriveDetailResponse extends BaseDTO<String> {

    private boolean allDay;

    private boolean allDayYes;

    private boolean allDayNo;

    private boolean morning;

    private boolean morningYes;

    private boolean morningNo;

    private boolean afternoon;

    private boolean afternoonYes;

    private boolean afternoonNo;

    private boolean evening;

    private boolean eveningYes;

    private boolean eveningNo;

    private String arriveContent;

    private String arriveUrlPicture;

    private LocalTime timeArriveKid;
}
