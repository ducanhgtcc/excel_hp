package com.example.onekids_project.mobile.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReplyTeacherObject {

    private String fullName;

    private String avatar;

    private String content;

    private String createdDate;

    private boolean modifyStatus;
}
