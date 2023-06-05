package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmployeeSalaryDefaultCreateRequest {
//
//    @NotNull
//    private Long idInfoEmployee;

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


}
