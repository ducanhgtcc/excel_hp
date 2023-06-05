package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.AppIconPlus;
import com.example.onekids_project.entity.school.AppIconTeacher;

public interface AppIconPlusRepositoryCustom {
    /**
     * tìm kiếm icon plus của một trường
     *
     * @param idSchool
     * @return
     */
    AppIconPlus findAppIconPlus(Long idSchool);
}
