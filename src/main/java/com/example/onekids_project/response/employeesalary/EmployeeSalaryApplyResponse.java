package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeSalaryApplyResponse extends IdResponse {

    private String fullName;

    private String phone;

    private double totalPay;

    List<FnEmployeeSalaryResponse> fnEmployeeSalaryList;

}
