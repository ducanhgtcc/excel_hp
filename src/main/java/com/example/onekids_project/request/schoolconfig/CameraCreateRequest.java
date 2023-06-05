package com.example.onekids_project.request.schoolconfig;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CameraCreateRequest {
    @NotBlank
    private String camName;

    @NotBlank
    private String linkCam;

    @NotBlank
    private String camChanel;

    @NotBlank
    private String camStream;

    private String camChanelOneCam;

    private String camStreamOneCam;

    private String note;

    private boolean camActive;

    @NotNull
    private Long idDvrcamera;

}
