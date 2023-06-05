package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.kids.AttendanceConfig;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceConfigCustom {
    /**
     * find cái được tạo gần nhất theo ngày tạo
     *
     * @param idSchool
     * @return
     */
    Optional<AttendanceConfig> findAttendanceConfigFinal(Long idSchool);

    Optional<AttendanceConfig> findAttendanceConfigDate(Long idSchool, LocalDate date);

    Optional<AttendanceConfig> findAttendanceConfigInDate(Long idSchool, LocalDate date);

}
