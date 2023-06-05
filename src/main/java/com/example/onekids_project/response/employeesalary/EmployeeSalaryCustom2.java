package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.finance.financegroup.SalaryGroupBriefResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-04-02 10:05
 *
 * @author lavanviet
 */
@Getter
@Setter
public class EmployeeSalaryCustom2 extends IdResponse {
    private String name;

    private String category;

    private String description;

    private String unit;

    private int number;

    private double price;

    private boolean discount;

    private double discountNumber;

    private double discountPrice;

    private boolean attendance;

    private boolean approved;

    private boolean locked;

    private double paid;

    private SalaryGroupBriefResponse fnSalaryGroup;
}
