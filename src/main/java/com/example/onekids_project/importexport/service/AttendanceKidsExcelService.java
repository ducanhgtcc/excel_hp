package com.example.onekids_project.importexport.service;

import com.example.onekids_project.importexport.model.AttendanceKidsModel;
import com.example.onekids_project.request.attendancekids.AttendanceKidsSearchCustomRequest;
import com.example.onekids_project.request.attendancekids.AttendanceKidsSearchDetailRequest;
import com.example.onekids_project.request.finance.order.OrderExcelRequest;
import com.example.onekids_project.response.excel.ExcelDynamicResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceKidsExcelService {
    /**
     * xuất file điểm danh theo ngày
     * @param attendanceKidsModelList
     * @param idSchool
     * @param idClass
     * @param currentDate
     * @return
     * @throws IOException
     */
    ByteArrayInputStream attendanceToExcel(List<AttendanceKidsModel> attendanceKidsModelList, Long idSchool, Long idClass, LocalDate currentDate) throws IOException;

    /**
     * xuất file điểm danh theo tháng
     * @param map
     * @param idSchool
     * @param idClass
     * @param date
     * @return
     * @throws IOException
     */
    ByteArrayInputStream attendanceToExcelMonth(Map<Long, List<AttendanceKidsModel>> map, Long idSchool, Long idClass, LocalDate date) throws IOException;

    ByteArrayInputStream exportAttendaceKidCustom(UserPrincipal principal, AttendanceKidsSearchCustomRequest request) throws IOException;

    List<ExcelNewResponse> exportAttendanceKidCustomNew(UserPrincipal principal, AttendanceKidsSearchCustomRequest request);
    ExcelDynamicResponse excelExportAttendanceKidsDetailService(AttendanceKidsSearchDetailRequest request);
}
