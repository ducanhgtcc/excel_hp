package com.example.onekids_project.mobile.plus.response.medicine;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicinePlusDetailResponse {

    private String kidName;

    private String className;

    private String avatarkid;

    private String avartarParent;

    private String parentName;

    private String  diseaseName;

    private String dateSick;

    private String content;

    private boolean confirmStatus;

    private boolean checkSchoolReply;

    private List<String> pictureList;

    private String createdDate;

    private List<ReplyMobilePlusObject> replyList;
}
