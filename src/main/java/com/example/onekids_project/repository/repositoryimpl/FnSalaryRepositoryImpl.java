package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.finance.employeesalary.FnSalary;
import com.example.onekids_project.repository.repositorycustom.FnSalaryRepositoryCustom;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FnSalaryRepositoryImpl extends BaseRepositoryimpl<FnSalary> implements FnSalaryRepositoryCustom {
    @Override
    public List<FnSalary> findBySchoolWithName(Long idSchool, String name) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (StringUtils.isNotBlank(name)) {
            queryStr.append("and lower(name) like lower(:name) ");
            mapParams.put("name", "%" + name.trim() + "%");
        }
        queryStr.append("order by category desc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
