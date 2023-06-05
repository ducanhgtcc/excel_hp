package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.VideoParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface VideoParentService {
    VideoParentResponse findVideoParent(UserPrincipal principal);

    List<String> findVideoOther(UserPrincipal principal);
}
