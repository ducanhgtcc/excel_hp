package com.example.onekids_project.service.servicecustom.employeesaraly;

import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.entity.finance.employeesalary.OrderEmployeeHistory;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.request.employeeSalary.SearchAttendanceSalaryRequest;
import com.example.onekids_project.response.employeesalary.OrderSalaryHistoryDetailResponse;
import com.example.onekids_project.response.employeesalary.OrderSalaryHistoryResponse;
import com.example.onekids_project.response.employeesalary.SalaryPackageHistoryPaymentResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * date 2021-03-03 4:46 CH
 *
 * @author ADMIN
 */

public interface OrderEmployeeHistoryService {
    OrderEmployeeHistory saveOrderEmployeeHistory(String category, String name, LocalDate date, double moneyInput, String description, CashBookHistory cashBookHistory, FnOrderEmployee fnOrderEmployee);

    List<OrderSalaryHistoryResponse> findOrderKidsHistory(UserPrincipal principal, Long idOrder);

    List<OrderSalaryHistoryDetailResponse> findOrderKidsHistoryDetail(UserPrincipal principal, Long idOrderHistory);

    List<SalaryPackageHistoryPaymentResponse> findSalaryPackagePaymentDetail(UserPrincipal principal, Long idSalaryPackage);

    ByteArrayInputStream exportAttendanceSalary(UserPrincipal principal, SearchAttendanceSalaryRequest request) throws IOException;

    List<ExcelNewResponse> exportAttendanceSalaryNew(UserPrincipal principal, SearchAttendanceSalaryRequest request);

}
