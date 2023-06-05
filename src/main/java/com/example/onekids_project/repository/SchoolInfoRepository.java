package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.SchoolInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * date 2021-03-05 15:24
 *
 * @author lavanviet
 */
public interface SchoolInfoRepository extends JpaRepository<SchoolInfo, Long> {
    Optional<SchoolInfo> findBySchoolId(Long idSchool);
}
