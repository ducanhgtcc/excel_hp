package com.example.onekids_project.master.response.feedback;

import com.example.onekids_project.response.common.FileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedbackDetailAdminResponse {
    private String type;

    private String createdBy;

    private LocalDateTime createdDate;

    private String hidden;

    private String feedbackTitle;

    private String feedbackContent;

    private List<FileResponse> fileList;

    private String schoolReply;

    private LocalDateTime confirmDate;

    private String confirmName;

    private LocalDateTime schoolTimeReply;

    private String replyName;

    private boolean schoolReplyDel;
}
