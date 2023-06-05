package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.user.ApiResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleConfigResponse extends IdResponse {
    private String roleName;

    private String type;

    private String description;

    private boolean defaultStatus;

    private List<ApiResponse> apiList;
}
