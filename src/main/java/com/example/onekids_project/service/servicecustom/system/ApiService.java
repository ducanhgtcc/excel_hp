package com.example.onekids_project.service.servicecustom.system;

import com.example.onekids_project.master.request.api.ApiUpdateRequest;
import com.example.onekids_project.master.response.api.ApiMasterResponse;
import com.example.onekids_project.request.finance.ChangeSortRequest;
import com.example.onekids_project.response.user.ApiResponse;
import com.example.onekids_project.security.model.UserPrincipal;

import java.util.List;

public interface ApiService {
    List<ApiResponse> searchApi(UserPrincipal principal, String type);

    List<ApiMasterResponse> searchApiMaster(UserPrincipal principal, String type);

    boolean updateApi(UserPrincipal principal, ApiUpdateRequest request);

    boolean updateApiRanks(UserPrincipal principal, ChangeSortRequest request);
}
