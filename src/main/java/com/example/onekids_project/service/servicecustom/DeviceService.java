package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.master.request.device.DeviceWebRequest;
import com.example.onekids_project.mobile.request.DeviceLoginMobileRequest;
import com.example.onekids_project.response.device.DeviceLoginResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface DeviceService {
    /**
     * login device
     * thực hiện bởi chính người dùng
     * @param deviceWebRequest
     * @return
     */
    boolean saveDeviceLogin(Long idUser, DeviceWebRequest deviceWebRequest);

    /**
     * logout device
     *
     * @param deviceWebRequest
     * @return
     */
    boolean saveDeviceLogout(Long idUser, DeviceWebRequest deviceWebRequest);
    void saveDeviceLogoutAdmin(Long idDevice);

    /**
     * login device
     *
     * @param deviceLoginMobileRequest
     * @return
     */
    boolean saveDeviceLoginMobile(Long idUser, DeviceLoginMobileRequest deviceLoginMobileRequest);

    void checkDeviceLimit(UserPrincipal principal, String idDevice);

    void forceLogoutDevice(String idDevice, Long idUser);
}
