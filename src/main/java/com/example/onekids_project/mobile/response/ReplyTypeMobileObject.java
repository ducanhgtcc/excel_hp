package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyTypeMobileObject {
    //parent, teacher, plus
    private String type;

    private String fullName;

    private String avatar;

    private String content;

    private LocalDateTime createdDate;

    private boolean modifyStatus;

    private boolean revoke;
}
