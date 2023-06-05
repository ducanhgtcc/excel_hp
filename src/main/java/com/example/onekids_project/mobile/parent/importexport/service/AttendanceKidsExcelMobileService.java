package com.example.onekids_project.mobile.parent.importexport.service;


import com.example.onekids_project.mobile.parent.importexport.model.AttendanceKidsExModel;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface AttendanceKidsExcelMobileService{

    /**
     * xuất file điểm danh theo tháng
     * @return
     * @throws IOException
     */
    ByteArrayInputStream attendanceToExcelMonth(UserPrincipal principal, List<AttendanceKidsExModel> attendanceKidsExModel, LocalDate date) throws IOException;
    /**
     * xuất file điểm danh theo tháng
     * @return
     * @throws IOException
     */
    ByteArrayInputStream attendanceEatToExcelMonth(UserPrincipal principal, List<AttendanceKidsExModel> attendanceKidsExModel, LocalDate date) throws IOException;

}
