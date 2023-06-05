package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EvaluateWeekResponse extends IdResponse {
    private LocalDate date;

    private boolean parentReplyDel;

    private String parentReplyContent;

    private boolean schoolReadReply;

    private boolean approved;

    private String content;

    private List<EvaluateWeekFileResponse> evaluateWeekFileList;

    private KidOtherResponse kids;

}
