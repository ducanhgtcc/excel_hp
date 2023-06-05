package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.AppIconPlus;
import com.example.onekids_project.entity.school.AppIconTeacher;
import com.example.onekids_project.repository.repositorycustom.AppIconPlusRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppIconPlusRepositoryImpl extends BaseRepositoryimpl<AppIconPlus> implements AppIconPlusRepositoryCustom {
    @Override
    public AppIconPlus findAppIconPlus(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        List<AppIconPlus> appIconPlusList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(appIconPlusList)) {
            return null;
        }
        return appIconPlusList.get(0);
    }
}
