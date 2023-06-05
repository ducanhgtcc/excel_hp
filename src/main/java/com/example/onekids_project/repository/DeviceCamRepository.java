package com.example.onekids_project.repository;

import com.example.onekids_project.entity.onecam.DeviceCam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author lavanviet
 */
public interface DeviceCamRepository extends JpaRepository<DeviceCam, Long> {
    Optional<DeviceCam> findByIdDeviceAndMaUserIdAndDelActiveTrue(String idDevice, Long idUser);
    Optional<DeviceCam> findByIdDeviceAndMaUserIdAndLoginTrueAndDelActiveTrue(String idDevice, Long idUser);

    List<DeviceCam> findByMaUserIdAndLoginTrueAndForceLogoutFalse(Long idUser);
}
