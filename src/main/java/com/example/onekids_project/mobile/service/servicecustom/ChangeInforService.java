package com.example.onekids_project.mobile.service.servicecustom;

import com.example.onekids_project.security.model.UserPrincipal;

public interface ChangeInforService {
    String findNewToken(UserPrincipal principal);
}
