package com.example.onekids_project.repository;

import com.example.onekids_project.entity.kids.AttendanceConfig;
import com.example.onekids_project.repository.repositorycustom.AttendanceConfigCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceConfigRepository extends JpaRepository<AttendanceConfig, Long>, AttendanceConfigCustom {
}
