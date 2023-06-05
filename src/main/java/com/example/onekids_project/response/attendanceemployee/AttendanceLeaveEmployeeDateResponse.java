package com.example.onekids_project.response.attendanceemployee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * date 2021-03-13 09:26
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceLeaveEmployeeDateResponse extends IdResponse {

    private Long idInfo;

    private LocalDate date;

    private Long idCreated;

    private Long idModified;

    private String lastModifieBy;

    private LocalDateTime lastModifieDate;

    private LocalDateTime createdDate;

    private String createdBy;

    private String leaveContent;

    private String leavePicture;

    private LocalTime leaveTime;

    private int minuteLeaveSoon;

    private boolean isLeave;

}
