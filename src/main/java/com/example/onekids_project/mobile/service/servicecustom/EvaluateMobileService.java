package com.example.onekids_project.mobile.service.servicecustom;

import com.example.onekids_project.mobile.response.EvaluateSampleMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface EvaluateMobileService {
    EvaluateSampleMobileResponse getEvaluateSample(UserPrincipal principal);
}
