package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EvaluateDateResponse extends IdResponse {
    private LocalDate date;
    private boolean approved;
    private boolean parentReplyDel;
    private String parentReplyContent;
    private boolean schoolReadReply;

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
    private KidOtherResponse kids;

}
