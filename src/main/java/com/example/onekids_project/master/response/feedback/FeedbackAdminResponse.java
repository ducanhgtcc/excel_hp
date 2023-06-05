package com.example.onekids_project.master.response.feedback;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FeedbackAdminResponse extends IdResponse {

    private LocalDateTime createdDate;

    private boolean hiddenStatus;

    private String createdBy;

    private String feedbackTitle;

    private String feedbackContent;

    private String replyName;

    private int fileNumber;

    private boolean schoolUnread;

    private boolean schoolConfirmStatus;


}
