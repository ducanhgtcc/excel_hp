package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeSalaryApplySearchRequest {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotBlank
    @StringInList(values = {EmployeeConstant.STATUS_WORKING, EmployeeConstant.STATUS_RETAIN, EmployeeConstant.STATUS_LEAVE})
    private String employeeStatus;

    private Long idDepartment;

    private String employeeNameOrPhone;

    private List<Long> idList;

}
