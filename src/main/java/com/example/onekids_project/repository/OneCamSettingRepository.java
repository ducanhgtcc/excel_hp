package com.example.onekids_project.repository;

import com.example.onekids_project.entity.onecam.OneCamSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author lavanviet
 */
public interface OneCamSettingRepository extends JpaRepository<OneCamSetting, Long> {
    Optional<OneCamSetting> findByMaClassId(Long idClass);
}
