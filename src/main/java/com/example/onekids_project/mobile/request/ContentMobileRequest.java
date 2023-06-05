package com.example.onekids_project.mobile.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
public class ContentMobileRequest {
    @NotBlank
    private String content;
}
