package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EmployeeSalaryApplyUpdateRequest extends IdRequest {

    private String name;

    private String unit;

    private String description;

    @NotNull
    private int number;

    @NotNull
    private double price;

    private boolean discount;

    //FinanceConstant: percent, money
    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean attendance;

    //FinanceConstant:
    private String attendanceType;

    //FinanceConstant:
    private String attendanceDetail;

}
