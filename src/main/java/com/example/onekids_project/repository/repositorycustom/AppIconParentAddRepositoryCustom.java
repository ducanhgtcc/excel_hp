package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.school.AppIconParentAdd;

public interface AppIconParentAddRepositoryCustom {
    AppIconParentAdd findAppIconParentByIdKid(Long idSchool, Long idKids);
}
