package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.AppIconParentAdd;
import com.example.onekids_project.repository.repositorycustom.AppIconParentAddRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppIconParentAddRepository extends JpaRepository<AppIconParentAdd, Long>, AppIconParentAddRepositoryCustom {
    /**
     * find appIconParent
     * @param idSchool
     * @param idKid
     * @return
     */
    Optional<AppIconParentAdd> findByIdSchoolAndKidId(Long idSchool, Long idKid);
}
