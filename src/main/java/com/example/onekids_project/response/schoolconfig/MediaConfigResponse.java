package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaConfigResponse extends IdResponse {
    private String mediaName;

    private String linkMedia;

    private String mediaType;

    private String note;

    private boolean mediaActive;

    private String scopeType;
}
