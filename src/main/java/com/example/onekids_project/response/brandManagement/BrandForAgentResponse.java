package com.example.onekids_project.response.brandManagement;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.schoolconfig.DvrCameraOtherResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandForAgentResponse extends IdResponse {

    private String banndName;

    private boolean used;
}
