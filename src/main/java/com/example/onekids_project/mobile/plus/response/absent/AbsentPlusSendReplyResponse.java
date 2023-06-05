package com.example.onekids_project.mobile.plus.response.absent;

import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AbsentPlusSendReplyResponse extends IdResponse {

    private String schoolReply;

    private List<SendReplyMobilePlusObject> replyList;
}
