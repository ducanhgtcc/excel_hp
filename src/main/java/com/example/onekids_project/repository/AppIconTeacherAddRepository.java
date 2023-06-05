package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.AppIconTeacherAdd;
import com.example.onekids_project.repository.repositorycustom.AppIconTeacherAddRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppIconTeacherAddRepository extends JpaRepository<AppIconTeacherAdd, Long>, AppIconTeacherAddRepositoryCustom {
    Optional<AppIconTeacherAdd> findByIdSchoolAndInfoEmployeeSchoolIdAndDelActiveTrue(Long idSchool, Long idInfoEmployeeSchool);
}
