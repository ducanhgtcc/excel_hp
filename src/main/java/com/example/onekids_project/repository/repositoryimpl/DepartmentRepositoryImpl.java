package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.repository.repositorycustom.DepartmentRepositoryCustom;
import com.example.onekids_project.request.base.PageNumberWebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentRepositoryImpl extends BaseRepositoryimpl<Department> implements DepartmentRepositoryCustom {

    @Override
    public List<Department> findDepartmentCommon(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by department_name collate utf8_vietnamese_ci");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Department> findAllDepartment(Long idSchool, PageNumberWebRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by department_name collate utf8_vietnamese_ci");
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<Department> findDepartment(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);

        queryStr.append(" order by department_name ASC");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }
}
