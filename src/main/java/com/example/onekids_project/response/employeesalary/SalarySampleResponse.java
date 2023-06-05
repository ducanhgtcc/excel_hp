package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.finance.financegroup.SalaryGroupBriefResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalarySampleResponse extends IdResponse {

    private String name;

    private String description;

    private String unit;

    private String category;

    private int number;

    private double price;

    private boolean discount;

    //FinanceConstant: percent, money
    private String discountType;

    private double discountNumber;

    private double discountPrice;

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

    private boolean active;

    private boolean attendance;

    //FinanceConstant:
    private String attendanceType;

    //FinanceConstant:
    private String attendanceDetail;

    private SalaryGroupBriefResponse fnSalaryGroup;

}
