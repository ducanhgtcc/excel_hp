package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.repository.repositorycustom.ExEmployeeClassRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExEmployeeClassRepositoryImpl extends BaseRepositoryimpl<ExEmployeeClass> implements ExEmployeeClassRepositoryCustom {
    @Override
    public List<Grade> findAllEmployeeClass(Long idSchool, Pageable pageable) {
        return null;
    }

    @Override
    public List<ExEmployeeClass> findByIdExEmployeeClass(Long idClass) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idClass != null) {
            queryStr.append("and id_class=:idClass");
            mapParams.put("idClass", idClass);
        }
        List<ExEmployeeClass> exEmployeeClassList = findAllNoPaging(queryStr.toString(), mapParams);
        return exEmployeeClassList;
    }

    @Override
    public ExEmployeeClass findByIdClassAndIdEmployee(Long idClass, Long idInfoEmployeeSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (idClass != null) {
            queryStr.append("and id_info_employee=:idInfoEmployeeSchool ");
            mapParams.put("idInfoEmployeeSchool", idInfoEmployeeSchool);
        }
        List<ExEmployeeClass> exEmployeeClassList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(exEmployeeClassList)) {
            return null;
        }
        return exEmployeeClassList.get(0);
    }
}
