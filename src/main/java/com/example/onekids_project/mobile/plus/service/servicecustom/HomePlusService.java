package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.response.home.HomeFirstPlusResponse;
import com.example.onekids_project.mobile.plus.response.home.HomePlusResponse;
import com.example.onekids_project.mobile.response.ChangeTokenResponse;
import com.example.onekids_project.mobile.response.NewsMobileResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface HomePlusService {
    HomeFirstPlusResponse getHomeFirstPlus(UserPrincipal principal);

    HomePlusResponse getHomePlus(UserPrincipal principal);

    String changeSchool(UserPrincipal principal, Long idSchool);

    List<NewsMobileResponse> findNews(UserPrincipal principal);

}
