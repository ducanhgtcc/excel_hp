package com.example.onekids_project.mobile.plus.response;

import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessagePlusDetailResponse {

    private String kidName;

    private String className;

    private String avatarkid;

    private String avartarParent;

    // ten nguoi gui
    private String parentName;

    private String content;

    private boolean confirmStatus;

    private List<String> pictureList;

    private String createdDate;

    private boolean checkSchoolReply;

    private List<ReplyMobilePlusObject> replyList;
}
