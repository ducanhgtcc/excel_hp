package com.example.onekids_project.mobile.plus.response.feedbackplus;

import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedbackPlusRevokeResponse {

    private List<SendReplyMobilePlusObject> replyList;
}
