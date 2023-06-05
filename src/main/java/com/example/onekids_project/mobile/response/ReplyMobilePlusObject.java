package com.example.onekids_project.mobile.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyMobilePlusObject {

    private String fullName;

    private String avatar;

    private String content;

    private String createdDate;

    private boolean modifyStatus;

    private boolean schoolMoidifystatus;

    private boolean statusDel;

    private String keyType;

    @JsonIgnore
    private LocalDateTime sortDate;

}
