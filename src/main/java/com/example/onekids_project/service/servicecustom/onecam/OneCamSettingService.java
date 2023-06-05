package com.example.onekids_project.service.servicecustom.onecam;

import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import com.example.onekids_project.response.onecam.OneCameSettingResponse;

import java.util.List;

/**
 * @author lavanviet
 */
public interface OneCamSettingService {
    void createOneCamSettingDefault(MaClass maClass);

    List<OneCameSettingResponse> searchOneCameSettingService(MediaSettingSearchRequest request);

    void updateOneCameSettingService(OneCameSettingResponse request);
}
