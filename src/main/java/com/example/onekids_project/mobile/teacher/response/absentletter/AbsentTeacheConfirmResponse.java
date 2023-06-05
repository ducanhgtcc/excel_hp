package com.example.onekids_project.mobile.teacher.response.absentletter;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AbsentTeacheConfirmResponse {

    private List<ReplyMobileDateObject> replyList;
}
