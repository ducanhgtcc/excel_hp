package com.example.onekids_project.mobile.plus.response;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.mobile.response.ReplyMobilePlusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessagePlusConfirmResponse {

    private List<ReplyMobilePlusObject> replyList;
}
