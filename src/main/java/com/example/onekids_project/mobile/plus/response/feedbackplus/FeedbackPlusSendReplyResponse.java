package com.example.onekids_project.mobile.plus.response.feedbackplus;

import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedbackPlusSendReplyResponse extends IdResponse {

    private String schoolReply;

    private List<SendReplyMobilePlusObject> replyList;
}
