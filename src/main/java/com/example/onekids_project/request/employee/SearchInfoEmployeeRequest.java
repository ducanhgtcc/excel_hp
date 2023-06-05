package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchInfoEmployeeRequest extends BaseRequest {

    private String employeeNameOrPhone;

    private String employeeStatus;

    private Long idDepartment;

    private String appType;

    private Long idSchool;


    @Override
    public String toString() {
        return "SearchInfoEmployeeRequest{" +
                "employeeNameOrPhone='" + employeeNameOrPhone + '\'' +
                ", employeeStatus='" + employeeStatus + '\'' +
                ", idDepartment=" + idDepartment +
                ", appType='" + appType + '\'' +
                ", idSchool=" + idSchool +
                "} " + super.toString();
    }
}
