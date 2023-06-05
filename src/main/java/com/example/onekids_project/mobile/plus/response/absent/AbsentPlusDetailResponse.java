package com.example.onekids_project.mobile.plus.response.absent;

import com.example.onekids_project.mobile.parent.response.absentletter.AbsentDateDataResponse;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AbsentPlusDetailResponse {

    private String kidName;

    private String className;

    private String avatarkid;

    private String avartarParent;

    private String parentName;

    private String dateAbsent;

    private String content;

    private boolean confirmStatus;

    private boolean checkSchoolReply;

    private List<String> pictureList;

    private List<AbsentDateDataResponse> absentDateList;

    private String createdDate;

    private List<ReplyMobilePlusObject> replyList;
}
