package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.AppIconPlus;
import com.example.onekids_project.repository.repositorycustom.AppIconPlusRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppIconPlusRepository extends JpaRepository<AppIconPlus, Long>, AppIconPlusRepositoryCustom {
    /**
     *
     * @param idSchool
     * @return
     */
    Optional<AppIconPlus> findBySchoolId(Long idSchool);
}
