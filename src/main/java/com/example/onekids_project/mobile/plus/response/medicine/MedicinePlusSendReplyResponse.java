package com.example.onekids_project.mobile.plus.response.medicine;

import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicinePlusSendReplyResponse extends IdResponse {

    private String schoolReply;

    private List<SendReplyMobilePlusObject> replyList;
}
