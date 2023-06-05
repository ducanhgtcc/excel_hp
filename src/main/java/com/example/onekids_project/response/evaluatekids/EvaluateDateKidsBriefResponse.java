package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EvaluateDateKidsBriefResponse extends IdResponse {
    private LocalDate date;

    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String sanitaryContent;

    private String healtContent;

    private String commonContent;

    private List<EvaluateAttachFileResponse> evaluateAttachFileList;

    private KidOtherResponse kids;

}
