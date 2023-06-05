package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EmployeeSalaryCreateRequest {

    @NotBlank
    @StringInList(values = {FinanceConstant.NOW_MONTH, FinanceConstant.NEXT_MONTH})
    private String status;

    @NotNull
    private Long idInfoEmployee;
}
