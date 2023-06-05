package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-04 2:28 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
public class SalaryPackagePaymentDetailResponse extends IdResponse {

    private boolean locked;

    private double paid;

    //(số lượng sử dụng*đơn giá)/số lượng mỗi đơn giá
    private double money;

    private String name;

    private String unit;

    private String category;
}
