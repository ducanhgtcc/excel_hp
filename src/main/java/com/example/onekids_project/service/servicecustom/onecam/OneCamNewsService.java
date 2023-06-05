package com.example.onekids_project.service.servicecustom.onecam;

import com.example.onekids_project.response.onecam.OneCamNewsResponse;

import java.util.List;

/**
 * @author lavanviet
 */
public interface OneCamNewsService {
    void createOneCamNews(Long idSchool);
    OneCamNewsResponse getOneCamNews();

    void updateOneCamNews(OneCamNewsResponse request);
}
