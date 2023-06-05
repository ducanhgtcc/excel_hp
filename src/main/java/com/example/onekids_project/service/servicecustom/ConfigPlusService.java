package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.response.schoolconfig.ConfigPlusResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface ConfigPlusService {
    void createConfigPlus(School school);

    ConfigPlusResponse getConfigPlusByIdSchool(UserPrincipal principal);
}
