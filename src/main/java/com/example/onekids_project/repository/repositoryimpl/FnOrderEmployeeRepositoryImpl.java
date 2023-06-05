package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.repository.repositorycustom.FnOrderEmployeeRepositoryCustom;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date 2021-02-24 9:52 SA
 *
 * @author ADMIN
 */
public class FnOrderEmployeeRepositoryImpl extends BaseRepositoryimpl<FnOrderEmployee> implements FnOrderEmployeeRepositoryCustom {
    @Override
    public List<FnOrderEmployee> searchOrderEmployeeYear(Long idInfoEmployee, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_info_employee=:idInfoEmployee ");
        mapParams.put("idInfoEmployee", idInfoEmployee);
        queryStr.append("and year=:year ");
        mapParams.put("year", year);
        queryStr.append("and view=true ");
        LocalDate nowDate = LocalDate.now();
        if (year >= nowDate.getYear()) {
            queryStr.append("and month<=:month ");
            mapParams.put("month", nowDate.getMonthValue());
        }
        queryStr.append("order by month desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<FnOrderEmployee> searchOrderWidthMonthYear(Long idInfoEmployee, int startMonth, int endMonth, int year) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_info_employee=:idInfoEmployee ");
        mapParams.put("idInfoEmployee", idInfoEmployee);
        queryStr.append("and year=:year ");
        mapParams.put("year", year);
        queryStr.append("and month>=:startMonth ");
        mapParams.put("startMonth", startMonth);
        queryStr.append("and month<=:endMonth ");
        mapParams.put("endMonth", endMonth);
        queryStr.append("order by month desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
