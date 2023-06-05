package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.AppIconParent;
import com.example.onekids_project.repository.repositorycustom.AppIconParentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppIconParentRepository extends JpaRepository<AppIconParent, Long>, AppIconParentRepositoryCustom {
    /**
     * find by idschool
     * @param idSchool
     * @return
     */
    Optional<AppIconParent> findBySchoolId(Long idSchool);
}
