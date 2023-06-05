package com.example.onekids_project.service.servicecustom.employeesaraly;

import com.example.onekids_project.request.finance.exportimport.ExportStatisticalSalaryRequest;
import com.example.onekids_project.request.finance.statistical.FinanceKidsStatisticalRequest;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.finance.statistical.FinanceKidsStatisticalResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

/**
 * date 2021-04-07 10:56
 *
 * @author lavanviet
 */
public interface FinanceStatisticalEmployeeService {
    FinanceKidsStatisticalResponse statisticalFinanceEmployee(UserPrincipal principal, FinanceKidsStatisticalRequest request);

    List<ExcelResponse> exportExcelOrder(UserPrincipal principal, ExportStatisticalSalaryRequest request);

}
