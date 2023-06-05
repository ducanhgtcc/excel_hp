package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EvaluateDateKidResponse extends IdResponse {
    private LocalDate date;

    private String learnCreatedBy;
    private String learnContent;

    private String eatCreatedBy;
    private String eatContent;

    private String sleepCreatedBy;
    private String sleepContent;

    private String sanitaryCreatedBy;
    private String sanitaryContent;

    private String healtCreatedBy;
    private String healtContent;

    private String commonCreatedBy;
    private String commonContent;

    private List<EvaluateAttachFileResponse> evaluateAttachFileList;

    private String parentReplyCreatedBy;
    private LocalDateTime parentReplyDatetime;
    private boolean schoolReadReply;
    private boolean parentReplyDel;
    private String parentReplyContent;

    private String teacherReplyCreatedBy;
    private LocalDateTime teacherReplyDatetime;
    private boolean teacherReplyDel;
    private String teacherReplyContent;

    private String schoolReplyCreatedBy;
    private LocalDateTime schoolReplyDatetime;
    private boolean schoolReplyDel;
    private String schoolReplyContent;
}
