package com.example.onekids_project.request.employeeSalary;

import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.request.finance.kidspackage.TransferNumberOneRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * date 2021-03-15 11:11
 *
 * @author lavanviet
 */
@Getter
@Setter
public class TransferNumberManyEmployeeRequest extends IdRequest {
    private List<TransferNumberOneRequest> fnEmployeeSalaryList;

    @Override
    public String toString() {
        return "TransferNumberManyRequest{" +
                "fnKidsPackageList=" + fnEmployeeSalaryList +
                "} " + super.toString();
    }
}
