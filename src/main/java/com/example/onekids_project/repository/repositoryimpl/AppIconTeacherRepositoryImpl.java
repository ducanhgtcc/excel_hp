package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.AppIconParent;
import com.example.onekids_project.entity.school.AppIconTeacher;
import com.example.onekids_project.repository.repositorycustom.AppIconParentRepositoryCustom;
import com.example.onekids_project.repository.repositorycustom.AppIconTeacherRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppIconTeacherRepositoryImpl extends BaseRepositoryimpl<AppIconTeacher> implements AppIconTeacherRepositoryCustom {
    @Override
    public AppIconTeacher findAppIconTeacher(Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        List<AppIconTeacher> appIconTeacherList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(appIconTeacherList)) {
            return null;
        }
        return appIconTeacherList.get(0);
    }
}
