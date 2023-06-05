package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyMobileObject {
    private String fullName;

    private String avatar;

    private String content;

    private LocalDateTime createdDate;

    private boolean modifyStatus;
}
