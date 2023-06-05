package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * date 2021-03-04 4:36 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
public class SalaryPackageHistoryPaymentResponse extends IdResponse {
    private double money;

    private OrderEmployeeHistoryCustom1 orderEmployeeHistory;
}

@Getter
@Setter
class OrderEmployeeHistoryCustom1 {
    private String name;

    private LocalDate date;

    private String description;
}
