package com.example.onekids_project.mobile.teacher.response.feedback;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedBackTeacherResponse extends IdResponse {
    private String title;

    private String content;

    private int replyNumber;

    private int pictureNumber;

    private String createdDate;

    private boolean confirmStatus;

    private boolean read;
}
