package com.example.onekids_project.importexport.service;

import com.example.onekids_project.request.classmenu.CreateFileExcelRequest;
import com.example.onekids_project.request.classmenu.CreateMultiClassMenu;
import com.example.onekids_project.request.schedule.CreateMultiSchedule;
import com.example.onekids_project.request.schedule.UploadScheduleRequest;
import com.example.onekids_project.response.schedule.ScheduleInClassWeekResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface SchedulesExcelService {

    /**
     * Xuất file excel thời khóa biểu của lớp theo tuần
     * @param scheduleInClassWeekResponseList
     * @param idSchool
     * @param idClass
     * @param currentDate
     * @return
     * @throws IOException
     */

    ByteArrayInputStream schedulesToExcel(List<ScheduleInClassWeekResponse> scheduleInClassWeekResponseList, Long idSchool, Long idClass, LocalDate currentDate ) throws IOException;
    CreateMultiSchedule saveScheduleFileExcel(Long idSchool, UploadScheduleRequest uploadScheduleRequest) throws IOException;

}
