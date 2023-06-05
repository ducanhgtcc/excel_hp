package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EmployeeSalaryApplyCreateRequest {

    @NotNull
    private LocalDate date;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String unit;

    @NotBlank
    @StringInList(values = {FinanceConstant.CATEGORY_IN, FinanceConstant.CATEGORY_OUT})
    private String category;

    private int number;

    private double price;

    private boolean discount;

    //FinanceConstant: percent, money
    @StringInList(values = {FinanceConstant.DISCOUNT_TYPE_PERCENT, FinanceConstant.DISCOUNT_TYPE_AMOUNT})
    private String discountType;

    private double discountNumber;

    private double discountPrice;

    private boolean attendance;

    //FinanceConstant:
    private String attendanceType;

    //FinanceConstant:
    private String attendanceDetail;


}
