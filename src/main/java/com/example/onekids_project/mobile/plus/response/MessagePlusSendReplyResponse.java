package com.example.onekids_project.mobile.plus.response;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessagePlusSendReplyResponse extends IdResponse {

    private String schoolReply;

    private List<SendReplyMobilePlusObject> replyList;
}
