package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.AppIconTeacher;
import com.example.onekids_project.repository.repositorycustom.AppIconTeacherRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppIconTeacherRepository extends JpaRepository<AppIconTeacher, Long>, AppIconTeacherRepositoryCustom {
    /**
     * find by idSchool
     * @param idSchool
     * @return
     */
    Optional<AppIconTeacher> findBySchoolId(Long idSchool);
}
