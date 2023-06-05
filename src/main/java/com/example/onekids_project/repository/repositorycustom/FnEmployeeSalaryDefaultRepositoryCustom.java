package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalaryDefault;

import java.util.List;

public interface FnEmployeeSalaryDefaultRepositoryCustom {
    List<FnEmployeeSalaryDefault> findByInfoAndMonth(Long id, int monthValue);
}
