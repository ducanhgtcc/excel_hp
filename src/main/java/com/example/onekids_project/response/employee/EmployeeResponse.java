package com.example.onekids_project.response.employee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponse extends IdResponse {
    private String employeeCode;

    private String fullName;
}
