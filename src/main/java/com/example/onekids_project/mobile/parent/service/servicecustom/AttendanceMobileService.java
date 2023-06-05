package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.importexport.model.AttendanceKidsExModel;
import com.example.onekids_project.mobile.parent.response.attendance.AttendanceEatResponse;
import com.example.onekids_project.mobile.parent.response.attendance.AttendanceMobileExcelResponse;
import com.example.onekids_project.mobile.parent.response.attendance.ListAttendanceMobileResponse;
import com.example.onekids_project.mobile.parent.response.attendance.ListAttendanceMonthResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceMobileService {
    /**
     * lấy điểm danh ngày
     *
     * @param principal
     * @param pageNumber
     * @param localDate
     * @return
     */
    ListAttendanceMobileResponse findAttendace(UserPrincipal principal, Integer pageNumber, LocalDate localDate);

    /**
     * find điểm danh tháng, lấy cả ngày hiện tại và tương lai
     *
     * @param principal
     * @param localDate
     * @return
     */
    ListAttendanceMonthResponse findAttendanceMonth(UserPrincipal principal, LocalDate localDate);

    /**
     * find điểm ăn danh tháng, chỉ lấy đến ngày hiện tại
     *
     * @param principal
     * @param localDate
     * @return
     */
    AttendanceEatResponse findAttendanceEatMonth(UserPrincipal principal, LocalDate localDate);

    List<AttendanceMobileExcelResponse> findExportMonthAttendace(UserPrincipal principal, LocalDate datePage, LocalDate localDate);

    List<AttendanceKidsExModel> detachedAttendanceKidsOfMonth(UserPrincipal principal, List<AttendanceMobileExcelResponse> listAttendanceKids);
}
