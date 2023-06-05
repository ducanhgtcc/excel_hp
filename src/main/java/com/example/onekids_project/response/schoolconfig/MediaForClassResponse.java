package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaForClassResponse extends IdResponse {
    private String mediaName;

    private String scopeType;

    private String linkMedia;

    private boolean used;
}
