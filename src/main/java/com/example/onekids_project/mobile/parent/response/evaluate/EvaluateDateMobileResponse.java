package com.example.onekids_project.mobile.parent.response.evaluate;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.ReplyTypeMobileObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EvaluateDateMobileResponse extends IdResponse {
    private LocalDate date;

    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String sanitaryContent;

    private String healtContent;

    private String commonContent;

    private List<AttachFileMobileResponse> fileList;

    private List<ReplyTypeMobileObject> replyList;

    public EvaluateDateMobileResponse() {
        this.fileList = new ArrayList<>();
        this.replyList = new ArrayList<>();
    }
}
