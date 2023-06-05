package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.master.request.CreateAppSendNotify;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import com.example.onekids_project.request.employee.SearchEmployeeRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepositoryCustom {
    /**
     * Tìm kiếm nhân viên
     *
     * @param searchEmployeeRequest
     * @param pageable
     * @return
     */
    List<Employee> findAllEmployee(SearchEmployeeRequest searchEmployeeRequest, Pageable pageable, Long idSchool);

    Optional<Employee> findByIdEmployee(Long idSchoolLogin, Long id);

    List<Employee> findAllEmployeeAppsend(List<Long> idSchoolList,String appTypeReceive) throws IOException;

    /**
     * Tạo thông báo cho list nhân viên, phòng ban
     */
    List<Employee> findAllEmployeeList(List<Long> idEmployeeList);
    List<Employee> findAllDeparmentList(List<Long> idDeparmentList);

    List<Employee> findAllEmployeeAAppsend(List<Long> idSchoolList, String teacher) throws IOException;
}
