package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.employeesalary.FnSalary;

import java.util.List;

public interface FnSalaryRepositoryCustom {
    List<FnSalary> findBySchoolWithName(Long idSchoolLogin, String name);
}
