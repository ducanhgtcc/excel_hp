package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CameraResponse extends IdResponse {
    private String camName;

    private String linkCam;

    private String camChanel;

    private String camStream;

    private String camChanelOneCam;

    private String camStreamOneCam;

    private String note;

    private boolean camActive;

    private DvrCameraOtherResponse dvrCamera;

}
