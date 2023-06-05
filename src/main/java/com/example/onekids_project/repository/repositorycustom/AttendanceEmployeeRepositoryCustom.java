package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.employee.AttendanceEmployee;

import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-09 3:42 CH
 *
 * @author ADMIN
 */
public interface AttendanceEmployeeRepositoryCustom {
    List<AttendanceEmployee> searchAttendanceEmployeeMonth(Long idSchool, LocalDate date);

    List<AttendanceEmployee> searchAttendanceEmployeeChart(Long idSchool, Long idDepartment, List<LocalDate> dateList);
}
