package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-04 10:43 SA
 *
 * @author ADMIN
 */
@Getter
@Setter
public class OrderSalaryHistoryDetailResponse extends IdResponse {
    private double money;

    //ten khoản thu
    private String name;
}
