package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmployeeConfigSearchRequest {

    @NotBlank
    @StringInList(values = {EmployeeConstant.STATUS_WORKING, EmployeeConstant.STATUS_RETAIN, EmployeeConstant.STATUS_LEAVE})
    private String employeeStatus;

    private Long idDepartment;

    private String employeeNameOrPhone;

}
