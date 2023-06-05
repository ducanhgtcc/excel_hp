package com.example.onekids_project.request.employee;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmployeeSearchNew extends PageNumberWebRequest {

    private Boolean loginStatus;

    @NotBlank
    @StringInList(values = {EmployeeConstant.STATUS_WORKING, EmployeeConstant.STATUS_RETAIN, EmployeeConstant.STATUS_LEAVE})
    private String employeeStatus;

    private Long idDepartment;

    private String employeeNameOrPhone;

    @Override
    public String toString() {
        return "EmployeeSearchNew{" +
                "employeeNameOrPhone='" + employeeNameOrPhone + '\'' +
                ", employeeStatus='" + employeeStatus + '\'' +
                ", idDepartment=" + idDepartment +
                "} " + super.toString();
    }
}
