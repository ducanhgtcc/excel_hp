package com.example.onekids_project.response.employee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoEmployeeSchoolOtherResponse extends IdResponse {
    private String fullName;

    private String employeeStatus;
}
