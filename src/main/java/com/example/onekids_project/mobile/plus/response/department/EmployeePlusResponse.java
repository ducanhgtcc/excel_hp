package com.example.onekids_project.mobile.plus.response.department;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePlusResponse extends IdResponse {

    private String employeeName;

    private String phone;

    private String avartar;

    private String className;

    private String loginStatus;
}
