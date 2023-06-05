package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.repository.repositorycustom.SchoolConfigRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolConfigRepository extends JpaRepository<SchoolConfig, Long>, SchoolConfigRepositoryCustom {

    /**
     * tìm kiếm theo id
     * @param id
     * @param delActive
     * @return
     */
    SchoolConfig findByIdAndDelActive(Long id, boolean delActive);

    /**
     * tìm kiếm cấu hình của một trường theo id trường
     * @param idSchool
     * @param delActive
     * @return
     */
    Optional<SchoolConfig> findBySchoolIdAndDelActive(Long idSchool, boolean delActive);
    /**
     * tìm kiếm cấu hình của một trường theo id trường
     * @param idSchool
     * @return
     */
    Optional<SchoolConfig> findBySchoolIdAndDelActiveTrue(Long idSchool);

    /**
     * find by idschool
     * @param idSchool
     * @return
     */
    Optional<SchoolConfig> findBySchoolId(Long idSchool);
}
