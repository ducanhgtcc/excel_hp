package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-02-22 3:16 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
public class EmployeeSalaryPaidResponse extends IdResponse {

    private String fullName;

    private String phone;

    private OrderEmployeeCustom1 billSampleResponse;

}
