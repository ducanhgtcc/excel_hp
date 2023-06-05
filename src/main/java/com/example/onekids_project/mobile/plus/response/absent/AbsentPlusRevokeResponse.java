package com.example.onekids_project.mobile.plus.response.absent;

import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AbsentPlusRevokeResponse {

    private List<SendReplyMobilePlusObject> replyList;
}
