package com.example.onekids_project.response.employeesalary;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-04-02 09:10
 *
 * @author lavanviet
 */
@Getter
@Setter
public class EmployeeSalarySettingResponse extends IdResponse {
    private String fullName;

    private String phone;

    List<EmployeeSalaryCustom2> fnEmployeeSalaryList;
}

