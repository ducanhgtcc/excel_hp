package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateMonthResponse extends IdResponse {
    private int month;

    private int year;

    private boolean parentReplyDel;

    private String parentReplyContent;

    private boolean schoolReadReply;

    private boolean approved;

    private String content;

    private List<EvaluateMonthFileResponse> evaluateMonthFileList;

    private KidOtherResponse kids;
}
