package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.entity.school.SchoolConfig;

public interface AttendanceConfigService {
    /**
     * tạo config điểm danh cho lần đầu khi tạo trường
     *
     * @param idSchool
     * @param schoolConfig
     */
    void createFirstAttendanceDateConfig(Long idSchool, SchoolConfig schoolConfig);
}
