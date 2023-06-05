package com.example.onekids_project.repository;

import com.example.onekids_project.entity.school.ConfigAttendanceEmployeeSchool;
import com.example.onekids_project.repository.repositorycustom.ConfigAttendanceEmployeeSchoolRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * date 2021-03-06 2:51 CH
 *
 * @author ADMIN
 */
public interface ConfigAttendanceEmployeeSchoolRepository extends JpaRepository<ConfigAttendanceEmployeeSchool, Long>, ConfigAttendanceEmployeeSchoolRepositoryCustom {
    Optional<ConfigAttendanceEmployeeSchool> findBySchoolId(Long idSchool);
}
