package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.response.NewsMobileResponse;

import java.util.List;

public interface NewsParentService {
    List<NewsMobileResponse> findNews();
}
