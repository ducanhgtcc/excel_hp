package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalaryDefault;
import com.example.onekids_project.repository.repositorycustom.FnEmployeeSalaryDefaultRepositoryCustom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FnEmployeeSalaryDefaultRepositoryImpl extends BaseRepositoryimpl<FnEmployeeSalaryDefault> implements FnEmployeeSalaryDefaultRepositoryCustom {
    @Override
    public List<FnEmployeeSalaryDefault> findByInfoAndMonth(Long id, int month) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (id != null) {
            queryStr.append("and id_info_employee=:id");
            mapParams.put("id", id);
        }
        queryStr.append("and month(created_date) =:month");
        mapParams.put("month", month);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
