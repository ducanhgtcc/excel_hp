package com.example.onekids_project.response.system;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
public class WebSystemTitleConfigResponse extends IdResponse {
    private String title;

    private String content;

    private String type;

    private String source;

    private boolean sms;

    private boolean firebase;

    private boolean ott;

    private String note1;

    private String note2;
}
