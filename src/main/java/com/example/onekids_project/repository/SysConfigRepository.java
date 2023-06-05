package com.example.onekids_project.repository;

import com.example.onekids_project.entity.system.SysConfig;
import com.example.onekids_project.repository.repositorycustom.SysConfigRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysConfigRepository extends JpaRepository<SysConfig, Long>, SysConfigRepositoryCustom {
    /**
     * tìm kiếm cấu hình hệ thống của một trường
     * @return
     */
    Optional<SysConfig> findBySchoolIdAndDelActive(Long idSchool, boolean delActive);

    /**
     * find by idschool
     * @param idSchool
     * @return
     */
    Optional<SysConfig> findBySchoolId(Long idSchool);
}
