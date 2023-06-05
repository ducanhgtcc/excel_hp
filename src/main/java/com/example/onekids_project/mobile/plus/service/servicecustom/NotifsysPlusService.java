package com.example.onekids_project.mobile.plus.service.servicecustom;

import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.NotifiSysRequest;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.ListNotifiSysResponse;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.NotifiSysDetailResponse;
import com.example.onekids_project.security.model.UserPrincipal;

public interface NotifsysPlusService {

    ListNotifiSysResponse searchNitifiSys(UserPrincipal principal, NotifiSysRequest request);

    NotifiSysDetailResponse findDetailNotifiSys(UserPrincipal principal, Long id);
}
