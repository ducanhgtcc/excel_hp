package com.example.onekids_project.importexport.service;

import com.example.onekids_project.importexport.model.*;
import com.example.onekids_project.request.employee.CreateEmployeeExcelRequest;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeExcelService {
    /**
     * xuất file excel nhân viên
     *
     * @param employeeModelList
     * @param nameSchool
     * @return
     * @throws IOException
     */
    ByteArrayInputStream customersToExcel(List<EmployeeModel> employeeModelList, String nameSchool) throws IOException;

    ListEmployeeModelImport importExcelEmployee(UserPrincipal principal, MultipartFile multipartFile) throws IOException;

    ByteArrayInputStream customEmployeeImportFailExcel(List<EmployeeModelImportFail> employeeModelImportFailList, String schoolName) throws IOException;

    ByteArrayInputStream exportAttendanceEmployeeDate(UserPrincipal principal, List<AttendanceEmployeeDate> modelList, LocalDate date) throws IOException;

    List<ExcelNewResponse> exportAttendanceEmployeeDateNew(UserPrincipal principal, List<AttendanceEmployeeDate> modelList, LocalDate date);

    ByteArrayInputStream exportAttendanceEmployeeMonth(UserPrincipal principal, List<AttendanceEmployeeMonth> modelList, LocalDate date) throws IOException;

    List<ExcelNewResponse> exportAttendanceEmployeeMonthNew(UserPrincipal principal, List<AttendanceEmployeeMonth> modelList, LocalDate date);

    void importExcelNewEmployee(UserPrincipal principal, CreateEmployeeExcelRequest request);
}
