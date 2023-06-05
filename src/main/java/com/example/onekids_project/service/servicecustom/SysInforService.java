package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.system.SysInforRequest;
import com.example.onekids_project.response.system.SysInforResponse;

public interface SysInforService {
    /**
     * lấy số điện thoại hỗ trợ của onekids
     *
     * @return
     */
    SysInforResponse getFirstSupportOnekids();

    /**
     * update systeminfo
     *
     * @param sysInforRequest
     * @return
     */
    SysInforResponse updateSystemInfor(SysInforRequest sysInforRequest);

    /**
     * create common
     *
     * @return
     */
    boolean createCommonALl();

}
