package com.example.onekids_project.mobile.teacher.response.message;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.response.ReplyMobileObject;
import com.example.onekids_project.mobile.teacher.response.album.ListPictureOtherRespone;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageTeacherDetailResponse {

    private String fullName;

    private String className;

    private String avatarkid;

    private String avartarParent;

    private String parentName;

    private String content;

    private boolean confirmStatus;

    private boolean checkTeacherReply;

    private List<String> pictureList;

    private String createdDate;

    private List<ReplyMobileDateObject> replyList;
}
