package com.example.onekids_project.response.employee;

import com.example.onekids_project.enums.EmployeeStatusEnum;
import com.example.onekids_project.enums.StudentStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeStatusResponse {
    private EmployeeStatusEnum key;

    private String value;
}
