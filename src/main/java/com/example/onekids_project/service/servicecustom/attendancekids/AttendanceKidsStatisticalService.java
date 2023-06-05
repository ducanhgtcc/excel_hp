package com.example.onekids_project.service.servicecustom.attendancekids;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.response.attendancekids.AttendanceKidsStatisticalResponse;

import java.time.LocalDate;

/**
 * date 2021-03-11 15:19
 *
 * @author lavanviet
 */
public interface AttendanceKidsStatisticalService {
    AttendanceKidsStatisticalResponse attendanceKidsStatistical(Kids kids, LocalDate startDate, LocalDate endDate);
}
