package com.example.onekids_project.mobile.parent.response.message;

import com.example.onekids_project.mobile.response.ReplyMobileObject;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageParentDetailMobileResponse {

    private String fullName;

    private String avatar;

    private String content;

    private List<String> pictureList;

    private boolean confirmStatus;

    private boolean schoolModifyStatus;

    private LocalDateTime createdDate;

    private List<ReplyMobileObject> replyList;
}
