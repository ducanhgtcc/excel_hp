package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.request.base.IdObjectRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

/**
 * date 2021-03-22 11:17
 *
 * @author Phạm Ngọc Thắng
 */
@Getter
@Setter
public class SalaryPackageCustom1 {
    @Valid
    private List<IdObjectRequest> fnEmployeeSalaryList;

    @Override
    public String toString() {
        return "SalaryPackageCustom1{" +
                "fnEmployeeSalaryList=" + fnEmployeeSalaryList +
                '}';
    }
}
