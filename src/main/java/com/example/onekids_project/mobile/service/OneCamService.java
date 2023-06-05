package com.example.onekids_project.mobile.service;

import com.example.onekids_project.mobile.response.onecam.ParentCamResponse;
import com.example.onekids_project.mobile.response.onecam.PlusCamResponse;
import com.example.onekids_project.mobile.response.onecam.TeacherCamResponse;

import java.util.List;

/**
 * @author lavanviet
 */
public interface OneCamService {

    List<ParentCamResponse> getCameraParentList(String idDevice);
    List<TeacherCamResponse> getCameraTeacherList(String idDevice);
    List<PlusCamResponse> getCameraPlusList(String idDevice);
    void logoutCameraService(String idDevice);




}
