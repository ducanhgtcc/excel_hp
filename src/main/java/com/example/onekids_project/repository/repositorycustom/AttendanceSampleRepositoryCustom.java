package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.sample.AttendanceSample;

import java.util.List;

public interface AttendanceSampleRepositoryCustom {
    /**
     * tìm kiếm mẫu điểm danh mặc định
     *
     * @param idSchool
     * @param idSystem
     * @return
     */
    List<AttendanceSample> findAllAttendanceSample(Long idSchool, Long idSystem);

    List<AttendanceSample> findAllAttendanceSampleWithType(Long idSchool, Long idSystem, String type);
}
