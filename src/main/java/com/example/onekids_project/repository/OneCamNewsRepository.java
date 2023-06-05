package com.example.onekids_project.repository;

import com.example.onekids_project.entity.onecam.OneCamNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lavanviet
 */
public interface OneCamNewsRepository extends JpaRepository<OneCamNews, Long> {
    Optional<OneCamNews> findByIdSchool(Long idSchool);
}
