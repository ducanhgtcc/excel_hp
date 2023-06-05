package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.finance.financegroup.SalaryGroupBriefResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmployeeStatisticalApplyResponse extends IdResponse {

    private String name;

    private String unit;

    private String category;

    private int year;

    private int month;

    private String description;

    private int number;

    private double price;

    private boolean discount;

    //FinanceConstant: percent, money
    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean approved;

    private Long idApproved;

    private LocalDateTime timeApproved;

    private boolean locked;

    private boolean attendance;

    //FinanceConstant:
    private String attendanceType;

    //FinanceConstant:
    private String attendanceDetail;

    private double paid;

    private SalaryGroupBriefResponse fnSalaryGroup;
}
