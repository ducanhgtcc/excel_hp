package com.example.onekids_project.request.employeeSalary;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * date 2021-02-18 4:29 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
public class SalaryStatusRequest {

    @Valid
    @NotEmpty
    private List<SalaryPackageCustom1> employeeList;

    @NotNull
    private Boolean status;

    @Override
    public String toString() {
        return "SalaryStatusRequest{" +
                "employeeList=" + employeeList +
                ", status=" + status +
                '}';
    }
}
