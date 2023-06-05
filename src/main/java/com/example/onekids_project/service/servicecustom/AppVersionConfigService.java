package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.request.system.AppVersionConfigRequest;
import com.example.onekids_project.response.system.AppVersionConfigResponse;

import java.util.List;

public interface AppVersionConfigService {
    /**
     * find all
     *
     * @return
     */
    List<AppVersionConfigResponse> findAllAppVersion();

    /**
     * update app version
     *
     * @param appVersionConfigRequest
     * @return
     */
    AppVersionConfigResponse updateAppVersion(AppVersionConfigRequest appVersionConfigRequest);
}
