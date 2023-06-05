package com.example.onekids_project.mobile.plus.response.feedbackplus;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackPlusResponse extends IdResponse {

    private String title;

    private String content;

    private int replyNumber;

    private int pictureNumber;

    private boolean confirmStatus;

    private String createdDate;

    private boolean schoolUnread;

}
