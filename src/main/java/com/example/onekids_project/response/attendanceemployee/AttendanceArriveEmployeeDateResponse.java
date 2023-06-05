package com.example.onekids_project.response.attendanceemployee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * date 2021-03-09 3:30 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
public class AttendanceArriveEmployeeDateResponse extends IdResponse {

    private Long idInfo;

    private LocalDate date;

    private boolean morning;

    private boolean morningYes;

    private boolean morningNo;

    private boolean afternoon;

    private boolean afternoonYes;

    private boolean afternoonNo;

    private boolean evening;

    private boolean eveningYes;

    private boolean eveningNo;

    private LocalTime arriveTime;

    private String arrivePicture;

    private String arriveContent;

    private int minuteArriveLate;

    private Long idCreated;

    private Long idModified;

    private String lastModifieBy;

    private LocalDateTime lastModifieDate;

    private LocalDateTime createdDate;

    private String createdBy;


}
