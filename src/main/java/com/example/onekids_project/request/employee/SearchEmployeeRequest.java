package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchEmployeeRequest extends BaseRequest {

    private String employeeNameOrPhone;

    private String employeeStatus;

    private Long idDepartment;
}
