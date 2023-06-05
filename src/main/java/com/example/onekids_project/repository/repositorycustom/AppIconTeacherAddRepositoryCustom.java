package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.AppIconTeacherAdd;

public interface AppIconTeacherAddRepositoryCustom {
    AppIconTeacherAdd findAppIconTeacherByIdEmployee(Long idSchool, Long idEmployee);
}
