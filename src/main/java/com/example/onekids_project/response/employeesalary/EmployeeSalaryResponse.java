package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeSalaryResponse extends IdResponse {

    private String fullName;

    private String phone;

    //tổng thu
    private double moneyTotalIn;

    //tổng chi
    private double moneyTotalOut;

    //còn lại phải thu: bằng hiệu còn lại thu-còn lại chi>0
    private double totalMoneyRemainIn;

    //còn lại phải chi: bằng hiệu còn lại thu-còn lại chi<0
    private double totalMoneyRemainOut;

//    //tổng thu
//    private double totalIn;
//
//    //tông chi
//    private double totalOut;

    private String numberApproved;

    private String numberLocked;

    List<EmployeeSalaryCustom1> fnEmployeeSalaryList;
}
