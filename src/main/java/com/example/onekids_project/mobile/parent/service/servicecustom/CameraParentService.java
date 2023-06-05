package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.CameraParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface CameraParentService {
    List<CameraParentResponse> findCameraParent(UserPrincipal principal);
}
