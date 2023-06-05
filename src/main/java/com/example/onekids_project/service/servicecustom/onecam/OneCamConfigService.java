package com.example.onekids_project.service.servicecustom.onecam;

import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.request.onecam.OneCamConfigRequest;
import com.example.onekids_project.response.onecam.OneCamConfigResponse;

/**
 * @author lavanviet
 */
public interface OneCamConfigService {
    void createOneCamConfigDefault(School school);

    OneCamConfigResponse getOneCamConfigService(Long idSchool);
    void getOneCamConfigService(OneCamConfigRequest request);


}
