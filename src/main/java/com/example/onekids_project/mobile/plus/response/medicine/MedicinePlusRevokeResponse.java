package com.example.onekids_project.mobile.plus.response.medicine;

import com.example.onekids_project.mobile.response.SendReplyMobilePlusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicinePlusRevokeResponse {

    private List<SendReplyMobilePlusObject> replyList;
}
