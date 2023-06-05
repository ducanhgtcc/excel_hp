package com.example.onekids_project.mobile.teacher.response.evaluate;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import com.example.onekids_project.mobile.response.ReplyTypeMobileObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateDateDetailKidTeacherResponse extends IdResponse {


    private String statusApprove;

    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String sanitaryContent;

    private String healtContent;

    private String commonContent;

    private List<AttachFileMobileResponse> fileList;

    private List<ReplyMobilePlusObject> replyList;

//    public EvaluateDateDetailKidTeacherResponse() {
//        this.fileList = new ArrayList<>();
//        this.replyList = new ArrayList<>();
//    }
}
