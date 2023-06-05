package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.AppIconPlusAdd;

public interface AppIconPlusAddRepositoryCustom {
    AppIconPlusAdd findAppIconPlusByIdEmployee(Long idSchool, Long idEmployee);
}
