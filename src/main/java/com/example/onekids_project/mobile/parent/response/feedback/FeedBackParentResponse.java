package com.example.onekids_project.mobile.parent.response.feedback;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedBackParentResponse extends IdResponse {
    private String title;

    private String content;

    private int replyNumber;

    private int pictureNumber;

    private LocalDateTime createdDate;

    private boolean confirmStatus;

    private boolean read;
}
