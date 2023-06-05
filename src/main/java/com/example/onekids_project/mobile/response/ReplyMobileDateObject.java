package com.example.onekids_project.mobile.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyMobileDateObject  {

    private String fullName;

    private String avatar;

    private String content;

    private String createdDate;

    private boolean modifyStatus;

    private boolean teacherModifyStatus;

    private boolean statusDel;

    private LocalDateTime sortDate;
}
