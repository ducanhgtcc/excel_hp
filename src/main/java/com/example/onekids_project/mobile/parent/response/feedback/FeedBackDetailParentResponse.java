package com.example.onekids_project.mobile.parent.response.feedback;

import com.example.onekids_project.mobile.response.ReplyMobileObject;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedBackDetailParentResponse {
    private String fullName;

    private String avatar;

    private String title;

    private String content;

    private List<String> pictureList;

    private boolean confirmStatus;

    private LocalDateTime createdDate;

    private List<ReplyMobileObject> replyList;
}
