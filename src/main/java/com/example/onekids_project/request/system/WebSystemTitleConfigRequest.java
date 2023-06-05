package com.example.onekids_project.request.system;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class WebSystemTitleConfigRequest extends IdRequest {
    @NotBlank
    private String title;

    private String content;

    private boolean sms;

    private boolean firebase;

    private boolean ott;

    private String note1;

    private String note2;
}
