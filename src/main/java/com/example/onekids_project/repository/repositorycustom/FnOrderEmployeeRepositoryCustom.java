package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;

import java.util.List;

/**
 * date 2021-02-24 9:52 SA
 *
 * @author ADMIN
 */
public interface FnOrderEmployeeRepositoryCustom {
    List<FnOrderEmployee> searchOrderEmployeeYear(Long idInfoEmployee, int year);

    List<FnOrderEmployee> searchOrderWidthMonthYear(Long idInfoEmployee, int startMonth, int endMonth, int year);
}
