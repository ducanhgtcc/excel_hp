package com.example.onekids_project.mobile.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppVersionResponse extends IdResponse {
    private String version;

    private boolean versionUpdate;

    private boolean compulsory;

    private String appType;

    private String appOs;

    private String apiUrl;

    private String updateContent;
}
