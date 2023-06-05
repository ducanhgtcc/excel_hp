package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EvaluateMonthKidResponse extends IdResponse {
    private int month;
    private int year;
    private String lastModifieBy;
    private LocalDateTime lastModifieDate;
    private String content;
    private List<EvaluateMonthFileResponse> evaluateMonthFileList;

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
