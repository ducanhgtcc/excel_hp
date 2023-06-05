package com.example.onekids_project.mobile.request;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ContentAndIdMobileRequest extends IdRequest {
    @NotBlank
    private String content;
}
