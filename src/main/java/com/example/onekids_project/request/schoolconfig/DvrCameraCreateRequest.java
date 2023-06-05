package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class DvrCameraCreateRequest {
    @NotBlank
    private String dvrName;

    @NotBlank
    private String dvrType;

    @NotBlank
    private String adminDvrAcc;

    @NotBlank
    private String adminDvrPassword;

    @NotBlank
    private String schoolDomain;

    @NotBlank
    private String camPort;

    @NotBlank
    private String linkDvr;

    private String ipLocal;

    private String modemAcc;

    private String modemPass;

    private String note;

    private boolean dvrActive;

    private String deviceSN;

    private String portDVR;
}
