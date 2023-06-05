package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.entity.school.AppIconTeacher;
import com.example.onekids_project.entity.school.AppIconTeacherAdd;
import com.example.onekids_project.repository.repositorycustom.AppIconTeacherAddRepositoryCustom;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppIconTeacherAddRepositoryImpl extends BaseRepositoryimpl<AppIconTeacherAdd> implements AppIconTeacherAddRepositoryCustom {
    @Override
    public AppIconTeacherAdd findAppIconTeacherByIdEmployee(Long idSchool, Long idEmployee) {
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
        List<AppIconTeacherAdd> appIconTeachertAddList = findAllNoPaging(queryStr.toString(), mapParams);
        if (CollectionUtils.isEmpty(appIconTeachertAddList)) {
            return null;
        }
        return appIconTeachertAddList.get(0);
    }
}
