package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CameraSettingResponse extends IdResponse {
    private String className;

    List<CameraOtherResponse> cameraList;
}
