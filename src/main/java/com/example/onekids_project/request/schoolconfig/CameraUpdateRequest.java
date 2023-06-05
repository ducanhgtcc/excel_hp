package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CameraUpdateRequest extends IdRequest {
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
