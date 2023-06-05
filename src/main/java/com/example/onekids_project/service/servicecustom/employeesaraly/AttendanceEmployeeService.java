package com.example.onekids_project.service.servicecustom.employeesaraly;

import com.example.onekids_project.entity.employee.AbsentTeacher;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.importexport.model.AttendanceEmployeeDate;
import com.example.onekids_project.importexport.model.AttendanceEmployeeMonth;
import com.example.onekids_project.request.attendanceemployee.*;
import com.example.onekids_project.request.employeeSalary.AttendanceEmployeeConfigRequest;
import com.example.onekids_project.request.employeeSalary.EmployeeConfigSearchRequest;
import com.example.onekids_project.response.attendanceemployee.*;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-08 1:59 CH
 *
 * @author ADMIN
 */
public interface AttendanceEmployeeService {
    List<AttendanceConfigEmployeeResponse> searchAttendanceConfigEmployee(UserPrincipal principal, EmployeeConfigSearchRequest request);

    boolean updateAttendanceConfigEmployee(UserPrincipal principal, Long id, AttendanceEmployeeConfigRequest request);

    List<AttendanceArriveEmployeeResponse> searchAttendanceArrive(UserPrincipal principal, AttendanceEmployeeSearchRequest request);

    List<AttendanceDetailDayEmployeeResponse> searchAttendanceDetailDay(UserPrincipal principal, AttendanceEmployeeSearchRequest request);

    boolean saveAttendanceArrive(UserPrincipal principal, AttendanceEmployeeArriveRequest request);

    boolean saveContentAttendanceArrive(UserPrincipal principal, AttendanceEmployeeContentRequest request) throws IOException;

    boolean saveMultiAttendanceArrive(UserPrincipal principal, List<AttendanceEmployeeArriveRequest> request);

    List<AttendanceLeaveEmployeeResponse> searchAttendanceLeave(UserPrincipal principal, AttendanceEmployeeSearchRequest request);

    boolean saveAttendanceLeave(UserPrincipal principal, AttendanceEmployeeLeaveRequest request);

    boolean saveMultiAttendanceLeave(UserPrincipal principal, List<AttendanceEmployeeLeaveRequest> request);

    boolean saveContentAttendanceLeave(UserPrincipal principal, AttendanceLeaveEmployeeContentRequest request) throws IOException;

    List<AttendanceEatEmployeeResponse> searchAttendanceEat(UserPrincipal principal, AttendanceEmployeeSearchRequest request);

    boolean saveAttendanceEat(UserPrincipal principal, AttendanceEmployeeEatRequest request);

    boolean saveAttendanceEatMulti(UserPrincipal principal, List<AttendanceEmployeeEatRequest> request);

    AttendanceEmployeesStatisticalResponse attendanceEmployeesStatistical(InfoEmployeeSchool infoEmployeeSchool, LocalDate startDate, LocalDate endDate);

    List<AttendanceEmployeeDate> searchAllAttendanceDate(UserPrincipal principal, AttendanceEmployeeSearchRequest request);

    List<AttendanceEmployeeMonth> searchAllAttendanceMonth(UserPrincipal principal, AttendanceEmployeeSearchRequest request);

    void createAttendanceEmployeeFromConfirm(AbsentTeacher absentTeacher);
}
