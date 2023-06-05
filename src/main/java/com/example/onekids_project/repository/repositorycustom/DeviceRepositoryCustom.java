package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepositoryCustom {
    List<Device> findTokenFireBaseByIdEmployee(Long idUser);
}
