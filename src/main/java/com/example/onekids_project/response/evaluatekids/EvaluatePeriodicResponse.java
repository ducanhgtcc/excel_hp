package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.entity.kids.EvaluatePeriodicFile;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EvaluatePeriodicResponse extends IdResponse {
    private LocalDate date;

    private boolean parentReplyDel;

    private String parentReplyContent;

    private boolean schoolReadReply;

    private boolean approved;

    private String content;

    private List<EvaluatePeriodicFileResponse> evaluatePeriodicFileList;

    private KidOtherResponse kids;
}
