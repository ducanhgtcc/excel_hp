package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.AppIconParent;

public interface AppIconParentRepositoryCustom {
    /**
     * tìm kiếm icon parent của một trường
     *
     * @param idSchool
     * @return
     */
    AppIconParent findAppIconParent(Long idSchool);
}
