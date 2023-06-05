package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;

import java.time.LocalDate;
import java.util.List;

public interface FnEmployeeSalaryRepositoryCustom {
    List<FnEmployeeSalary> findPackageApply(Long idInfoEmployee, LocalDate localDate);

    List<FnEmployeeSalary> getEmployeeSalaryApprovedWithMonth(Long idInfoEmployee, int month, int year, String category);
}
