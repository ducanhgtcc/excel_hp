package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.entity.school.DvrCamera;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CameraForClassResponse extends IdResponse {
    private String camName;

    private String linkCam;

    private DvrCameraOtherResponse dvrCamera;

    private boolean used;
}
