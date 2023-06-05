package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DvrCameraOtherResponse extends IdResponse {
    private String dvrName;

    private String linkDvr;
}
