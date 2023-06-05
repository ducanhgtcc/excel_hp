package com.example.onekids_project.repository;

import com.example.onekids_project.entity.sample.AttendanceSample;
import com.example.onekids_project.repository.repositorycustom.AttendanceSampleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceSampleRepository extends JpaRepository<AttendanceSample, Long>, AttendanceSampleRepositoryCustom {
    /**
     * tìm kiếm mẫu điểm danh theo id
     *
     * @param id
     * @param idSchool
     * @return
     */
    Optional<AttendanceSample> findByIdAndIdSchoolAndDelActiveTrue(Long id, Long idSchool);

    /**
     * tìm kiếm mẫu điểm danh cho hệ thống
     *
     * @param idSystem
     * @return
     */
    List<AttendanceSample> findByIdSchoolAndDelActiveTrueOrderByIdDesc(Long idSystem);

    List<AttendanceSample> findByIdSchoolAndDelActiveTrueAndAttendanceTypeOrderByIdDesc(Long idSystem, String type);

}
