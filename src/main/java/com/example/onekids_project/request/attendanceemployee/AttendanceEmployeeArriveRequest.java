package com.example.onekids_project.request.attendanceemployee;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * date 2021-03-12 09:49
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEmployeeArriveRequest {

    private Long id;

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
}
