package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.repository.repositorycustom.FnEmployeeSalaryRepositoryCustom;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FnEmployeeSalaryRepositoryImpl extends BaseRepositoryimpl<FnEmployeeSalary> implements FnEmployeeSalaryRepositoryCustom {
    @Override
    public List<FnEmployeeSalary> findPackageApply(Long idInfoEmployee, LocalDate localDate) {
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_info_employee=:idInfoEmployee ");
        mapParams.put("idInfoEmployee", idInfoEmployee);
        queryStr.append("and year=:year ");
        mapParams.put("year", year);
        queryStr.append("and month=:month ");
        mapParams.put("month", month);
        queryStr.append("order by (Select first_name from info_employee_school model1 where model1.id=model.id_info_employee) collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnEmployeeSalary> getEmployeeSalaryApprovedWithMonth(Long idInfoEmployee, int month, int year, String category) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_info_employee=:idInfoEmployee ");
        mapParams.put("idInfoEmployee", idInfoEmployee);
        queryStr.append("and year=:year ");
        mapParams.put("year", year);
        queryStr.append("and month=:month ");
        mapParams.put("month", month);
        if (FinanceConstant.CATEGORY_IN.equals(category) || FinanceConstant.CATEGORY_OUT.equals(category)) {
            queryStr.append("and category=:category ");
            mapParams.put("category", category);
        }
        queryStr.append("and approved=:approved ");
        mapParams.put("approved", AppConstant.APP_TRUE);
        queryStr.append("order by category desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
