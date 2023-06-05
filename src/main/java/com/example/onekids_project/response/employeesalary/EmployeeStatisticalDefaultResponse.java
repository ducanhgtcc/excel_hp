package com.example.onekids_project.response.employeesalary;


import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.finance.financegroup.SalaryGroupBriefResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeStatisticalDefaultResponse extends IdResponse {
    private String name;

    // in , out
    private String category;

    private String description;

    private String unit;

    private int number;

    private double price;

    private boolean discount;

    //FinanceConstant: percent, money
    private String discountType;

    private double discountNumber;

    private double discountPrice;

    // được áp dụng hay không
    private boolean active;

    private boolean attendance;

    private boolean t1;

    private boolean t2;

    private boolean t3;

    private boolean t4;

    private boolean t5;

    private boolean t6;

    private boolean t7;

    private boolean t8;

    private boolean t9;

    private boolean t10;

    private boolean t11;

    private boolean t12;

    //FinanceConstant:
    private String attendanceType;

    //FinanceConstant:
    private String attendanceDetail;

    private SalaryGroupBriefResponse fnSalaryGroup;

}

