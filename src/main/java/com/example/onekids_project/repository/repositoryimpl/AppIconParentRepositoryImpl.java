package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.AppIconParent;
import com.example.onekids_project.repository.repositorycustom.AppIconParentRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppIconParentRepositoryImpl extends BaseRepositoryimpl<AppIconParent> implements AppIconParentRepositoryCustom {
    @Override
    public AppIconParent findAppIconParent(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        List<AppIconParent> appIconParentList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(appIconParentList)) {
            return null;
        }
        return appIconParentList.get(0);
    }
}
