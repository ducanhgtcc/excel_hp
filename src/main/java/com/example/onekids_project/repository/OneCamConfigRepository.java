package com.example.onekids_project.repository;

import com.example.onekids_project.entity.onecam.OneCamConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lavanviet
 */
public interface OneCamConfigRepository extends JpaRepository<OneCamConfig, Long> {
    Optional<OneCamConfig> findBySchoolId(Long idSchool);
}
