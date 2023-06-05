package com.example.onekids_project.service.servicecustom;

import com.example.onekids_project.response.home.StatisticalClassHomeResponse;
import com.example.onekids_project.response.home.StatisticalHomeResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface HomeWebService {
    /**
     * thống kế màn home của web cả trường
     *
     * @param principal
     * @return
     */
    StatisticalHomeResponse findStatisticalTotalHome(UserPrincipal principal);

    List<StatisticalClassHomeResponse> findStatisticalClassHome(UserPrincipal principal);

}
