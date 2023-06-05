package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.AppIconPlusAdd;
import com.example.onekids_project.repository.repositorycustom.AppIconPlusAddRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppIconPlusAddRepositoryImpl extends BaseRepositoryimpl<AppIconPlusAdd> implements AppIconPlusAddRepositoryCustom {
    @Override
    public AppIconPlusAdd findAppIconPlusByIdEmployee(Long idSchool, Long idEmployee) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idEmployee != null) {
            queryStr.append("and id_info_employee=:idEmployee ");
            mapParams.put("idEmployee", idEmployee);
        }
        List<AppIconPlusAdd> appIconPlusAddList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(appIconPlusAddList)) {
            return null;
        }
        return appIconPlusAddList.get(0);
    }
}
