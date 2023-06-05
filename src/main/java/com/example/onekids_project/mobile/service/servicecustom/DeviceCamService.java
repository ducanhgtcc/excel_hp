package com.example.onekids_project.mobile.service.servicecustom;

import com.example.onekids_project.security.model.UserPrincipal;

/**
 * @author lavanviet
 */
public interface DeviceCamService {
    void saveDeviceCame(Long idUser, String idDevice, String deviceType);

    void checkLogoutDeviceCame(String idDevice);

    void forceLogoutCame(Long id);

    void checkDeviceCamLimit(UserPrincipal principal);

    void logoutCamService(String idDevice);


}
