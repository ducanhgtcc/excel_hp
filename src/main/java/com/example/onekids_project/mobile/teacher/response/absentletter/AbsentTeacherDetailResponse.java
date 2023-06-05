package com.example.onekids_project.mobile.teacher.response.absentletter;

import com.example.onekids_project.entity.kids.AbsentDate;
import com.example.onekids_project.mobile.parent.response.absentletter.AbsentDateDataResponse;
import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AbsentTeacherDetailResponse {

    private String fullName;

    private String className;

    private String avatarkid;

    private String avartarParent;

    private String parentName;

    private String dateAbsent;

    private String content;

    private boolean confirmStatus;

    private boolean checkTeacherReply;

    private List<String> pictureList;

    private List<AbsentDateDataResponse> absentDateList;

    private String createdDate;

    private List<ReplyMobileDateObject> replyList;
}
