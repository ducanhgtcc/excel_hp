package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.AppIconParentAdd;
import com.example.onekids_project.repository.repositorycustom.AppIconParentAddRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppIconParentAddRepositoryImpl extends BaseRepositoryimpl<AppIconParentAdd> implements AppIconParentAddRepositoryCustom {
    @Override
    public AppIconParentAdd findAppIconParentByIdKid(Long idSchool, Long idKids) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idKids != null) {
            queryStr.append("and id_kids=:idKids ");
            mapParams.put("idKids", idKids);
        }
        List<AppIconParentAdd> appIconParentAddList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(appIconParentAddList)) {
            return null;
        }
        return appIconParentAddList.get(0);
    }
}
