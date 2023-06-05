package com.example.onekids_project.request.system;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AppVersionConfigRequest extends IdRequest {
    @NotBlank
    private String version;

    private boolean versionUpdate;

    private boolean compulsory;

    @NotBlank
    private String updateContent;

    @NotBlank
    private String apiUrl;
}
