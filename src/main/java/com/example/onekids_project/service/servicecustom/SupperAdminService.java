package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.security.payload.AdminDataRequest;

public interface SupperAdminService {
    /**
     * create data
     *
     * @param adminDataRequest
     * @return
     */
    boolean createSupperAdmin(AdminDataRequest adminDataRequest);

}
