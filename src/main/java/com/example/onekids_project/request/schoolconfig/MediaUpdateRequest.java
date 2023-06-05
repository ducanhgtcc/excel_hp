package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MediaUpdateRequest extends IdRequest {
    @NotBlank
    private String mediaName;

    @NotBlank
    private String linkMedia;

    @NotBlank
    private String mediaType;

    @NotBlank
    private String scopeType;

    private String note;

    private boolean mediaActive;


}
