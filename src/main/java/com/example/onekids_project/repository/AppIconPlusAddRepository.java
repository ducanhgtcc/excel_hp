package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.AppIconPlus;
import com.example.onekids_project.entity.school.AppIconPlusAdd;
import com.example.onekids_project.repository.repositorycustom.AppIconPlusAddRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppIconPlusAddRepository extends JpaRepository<AppIconPlusAdd, Long>, AppIconPlusAddRepositoryCustom {
    Optional<AppIconPlusAdd> findByInfoEmployeeSchoolId(Long id);
}
