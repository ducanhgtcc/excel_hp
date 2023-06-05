package com.example.onekids_project.response.attendanceemployee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * date 2021-03-25 09:22
 *
 * @author Phạm Ngọc Thắng
 */

@Getter
@Setter
public class AttendanceDetailResponse extends IdResponse {

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

    private String arriveContent;

    private String arrivePicture;

    private LocalTime arriveTime;

    private int minuteArriveLate;

    private int minuteLeaveSoon;

    private String leaveContent;

    private String leavePicture;

    private LocalTime leaveTime;

    private Long idCreated;

    private String createdBy;

    private LocalDateTime createdDate;

    private Long idModified;

    private String lastModifieBy;

    private LocalDateTime lastModifieDate;

}
