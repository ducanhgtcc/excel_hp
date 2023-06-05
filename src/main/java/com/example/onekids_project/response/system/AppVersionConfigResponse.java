package com.example.onekids_project.response.system;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppVersionConfigResponse extends IdResponse {
    private String version;

    private boolean versionUpdate;

    private boolean compulsory;

    private String appType;

    private String appOs;

    private String updateContent;

    private String apiUrl;
}
