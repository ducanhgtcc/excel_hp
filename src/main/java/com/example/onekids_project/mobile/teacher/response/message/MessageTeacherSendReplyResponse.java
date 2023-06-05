package com.example.onekids_project.mobile.teacher.response.message;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageTeacherSendReplyResponse extends IdResponse {

    private String teacherReply;

    private List<ReplyMobileDateObject> replyList;
}
