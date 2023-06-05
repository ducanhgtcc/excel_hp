package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.system.SystemConfigSchoolTotalRequest;
import com.example.onekids_project.response.system.SysConfigResponse;
import com.example.onekids_project.response.system.SystemConfigSchoolTotalResponse;

public interface SysConfigService {
    /**
     * tạo config cho trường đó
     *
     * @param idSchool
     * @return
     */
    SysConfigResponse createSysConfigForSchool(Long idSchool);

    /**
     * find for sysconfig school
     *
     * @param idSchool
     * @return
     */
    SystemConfigSchoolTotalResponse findSystemConfigSchool(Long idSchool);

    /**
     * update systemconfig and school config
     *
     * @param systemConfigSchoolTotalRequest
     * @return
     */
    boolean updateSystemConfigSchool(SystemConfigSchoolTotalRequest systemConfigSchoolTotalRequest);

}
