package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.entity.user.FeedBackFile;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FeedbackDTO extends IdDTO {

    private String feedbackTitle;

    private String feedbackContent;

    private String urlPicture;

    private String schoolReply;

    private Boolean schoolConfirmStatus;

    private boolean schoolReplyDel;

    private Long idSchoolConfirm;

    private Long idSchoolReply;

    private String confirmName;

    private String replyName;

    private LocalDateTime confirmDate;

    private LocalDateTime schoolTimeReply;

    private List<FeedBackFile> feedBackFileList;
}
