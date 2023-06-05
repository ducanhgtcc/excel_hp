package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.master.request.employee.EmployeeSearchAdminRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import com.example.onekids_project.request.employee.*;
import com.example.onekids_project.request.kids.ExcelGroupOutRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InfoEmployeeSchoolRepositoryCustom {

    List<InfoEmployeeSchool> search(Long idSchool, EmployeeSearchNew search);

    long countEmployee(Long idSchool, EmployeeSearchNew search);

    List<InfoEmployeeSchool> searchPlus(Long idSchool, EmployeePlusSearchNew search);

    long countEmployeePlus(Long idSchool, EmployeePlusSearchNew search);

    List<InfoEmployeeSchool> searchEmployeeAdmin(EmployeeSearchAdminRequest search, List<Long> idSchoolList);

    long countEmployeeAdmin(EmployeeSearchAdminRequest search, List<Long> idSchoolList);


    /**
     * Tìm kiếm tất cả nhân viên trong trường
     *
     * @param searchInfoEmployeeRequest
     * @param pageable
     * @param idSchool
     * @return
     */
    List<InfoEmployeeSchool> findAllInfoEmployeeSchool(SearchInfoEmployeeRequest searchInfoEmployeeRequest, Pageable pageable, Long idSchool);

    /**
     * Tìm kiếm tất cả nhân viên trong trường theo các fied khác nhau
     *
     * @param searchExportEmployeeRequest
     * @param idSchool
     * @return
     */

    List<InfoEmployeeSchool> findAllInfoEmployeeSchoolByDepartment(Long idSchool, SearchExportEmployeeRequest searchExportEmployeeRequest);


    List<InfoEmployeeSchool> findAllInfoEmployeeSchoolAppSend(List<Long> idSchoolList, List<Long> idClassList);

    /**
     * lấy danh bạ giáo viên cho app giáo viên
     *
     * @param idSchool
     * @return
     */
    List<InfoEmployeeSchool> findContactTeacher(Long idSchool, Integer pageNumber);


    List<InfoEmployeeSchool> findInfoEmployeeDeparmentList(Long idSchool, List<Long> dataDepartmentNotifyList);

    List<InfoEmployeeSchool> findInfoEmployeeSchool(Long idSchool, List<Long> dataEmployeeNotifyList);

    List<InfoEmployeeSchool> searchEmplyeenew(Long idSchool, SearchTeacherBirthDayRequest searchTeacherBirthDayRequest);

    List<InfoEmployeeSchool> searchEmplyeenewa(Long idSchool, SearchTeacherBirthDayRequest request);

    List<InfoEmployeeSchool> findEmployeeAllBirthdayNoSchool(LocalDate localDate);

    long countSearchTeacherBirthday(Long idSchool, SearchTeacherBirthDayRequest request);

    List<InfoEmployeeSchool> searchEmployeeSaraly(Long idSchoolLogin, String employeeStatus, Long idDepartment, String employeeNameOrPhone, LocalDate date);

    List<InfoEmployeeSchool> searchEmployeeSalaryNew(Long idSchool, String employeeStatus, Long idDepartment, String nameOrPhone, List<Long>... idList);

    List<InfoEmployeeSchool> searchEmployeeWidthStatus(Long idSchool, String status);

    List<InfoEmployeeSchool> findEmployeeRetain(Long idSchool, String employeeStatusRetain);

    List<InfoEmployeeSchool> findEmployeeLeave(Long idSchool, String employeeStatusLeave);

    List<InfoEmployeeSchool> findAllInSchool(Long idSchool);

    List<InfoEmployeeSchool> findByEmployeeTimeLineWithDateAndNameOrPhone(Long idSchool, LocalDate date, String nameOrPhone);

    List<InfoEmployeeSchool> findByEmployeeTimeLineWithDate(Long idSchool, LocalDate date);

    List<InfoEmployeeSchool> searchGroupOut(Long idSchool, SearchEmployeeGroupOutRequest request);

    List<InfoEmployeeSchool> searchGroupOutExcel(Long idSchool, ExcelGroupOutRequest request);

    long countGroupOut(Long idSchool, SearchEmployeeGroupOutRequest request);

    List<InfoEmployeeSchool> getInfoListForCelebrateAuto(Long idSchool, String type, String gender);
}
