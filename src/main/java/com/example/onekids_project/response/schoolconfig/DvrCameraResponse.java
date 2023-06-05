package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DvrCameraResponse extends IdResponse {
    private String dvrName;

    private String dvrType;

    private String adminDvrAcc;

    private String adminDvrPassword;

    private String schoolDomain;

    private String camPort;

    private String linkDvr;

    private String ipLocal;

    private String modemAcc;

    private String modemPass;

    private String note;

    private boolean dvrActive;

    private String deviceSN;

    private String portDVR;
}
