package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.common.FileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedBackOnekidsResponse extends IdResponse {

    private String feedbackTitle;

    private String feedbackContent;

    private String accountType;

    private boolean parentUnread;

    private String schoolConfirmStatusString;

    private boolean schoolConfirmStatus;

    private String schoolUnreadString;

    private boolean schoolUnread;

    private LocalDateTime confirmDate;

    private String schoolConfirmString;
    private Long idSchoolConfirm;

    private Long idSchoolReply;
    private String schoolReplyName;

    private String schoolReply;

    private LocalDateTime schoolTimeReply;

    private Boolean schoolReplyDel;

    private boolean schoolModifyStatus;

    private String defaultContentDel;

    private String createdBy;

    private LocalDateTime createdDate;

    private int countReply;

    private int countPicture;

    private String urlPicture;

    private Long idKid;

    private int numberFile;

    private List<FileResponse> fileList;
}
