package com.example.onekids_project.response.employeesalary;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-04 2:28 CH
 *
 * @author ADMIN
 */
@Getter
@Setter
public class ListOrderSalaryDetailResponse {

    private String code;

    private String description;

    private List<SalaryPackagePaymentDetailResponse> dataList;
}
