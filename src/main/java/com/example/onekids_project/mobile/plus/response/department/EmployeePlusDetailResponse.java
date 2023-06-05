package com.example.onekids_project.mobile.plus.response.department;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeePlusDetailResponse {

    private String fullName;

    private String avatar;

    private String birthDay;

    private String phone;

    private String email;

    private String gender;

    private List<String> departmentEmployeeList;

    private List<String> employeeClassResponseList;

    private List<String> accountTypeList;

    private String educationLevel;

    private String startDate;

    private String contractDate;

    private String key;

    private String address;


}
