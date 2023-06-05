package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.AppIconTeacher;

public interface AppIconTeacherRepositoryCustom {
    /**
     * tìm kiếm icon teacher của một trường
     *
     * @param idSchool
     * @return
     */
    AppIconTeacher findAppIconTeacher(Long idSchool);
}
