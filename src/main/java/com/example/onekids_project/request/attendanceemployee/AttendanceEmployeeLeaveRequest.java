package com.example.onekids_project.request.attendanceemployee;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * date 2021-03-15 09:35
 *
 * @author Phạm Ngọc Thắng
 */

@Data
public class AttendanceEmployeeLeaveRequest {
    
    private Long idInfo;

    private LocalDate date;

    private String leaveContent;

    private String leavePicture;

    private LocalTime leaveTime;

    private int minuteLeaveSoon;


}
