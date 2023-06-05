package com.example.onekids_project.response.classes;

import com.example.onekids_project.response.accounttype.AccountTypeOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeInClassResponse {
    private String fullName;

    private String phone;

    private String employeeStatus;

    private List<AccountTypeOtherResponse> accountTypeList;
}
