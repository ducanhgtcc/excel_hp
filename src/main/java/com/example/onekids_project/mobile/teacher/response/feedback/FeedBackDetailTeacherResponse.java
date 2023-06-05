package com.example.onekids_project.mobile.teacher.response.feedback;

import com.example.onekids_project.mobile.response.ReplyMobileObject;
import com.example.onekids_project.mobile.response.ReplyTeacherObject;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedBackDetailTeacherResponse {
    private String fullName;

    private String avatar;

    private String title;

    private String content;

    private List<String> pictureList;

    private boolean confirmStatus;

    private String createdDate;

    private List<ReplyTeacherObject> replyList;
}
