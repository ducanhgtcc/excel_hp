package com.example.onekids_project.mobile.plus.response;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessagePlusRevokeResponse {

    private List<SendReplyMobilePlusObject> replyList;
}
