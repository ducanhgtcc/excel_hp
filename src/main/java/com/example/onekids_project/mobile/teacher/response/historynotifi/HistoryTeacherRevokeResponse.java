package com.example.onekids_project.mobile.teacher.response.historynotifi;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HistoryTeacherRevokeResponse {

    private List<ReplyMobileDateObject> replyList;
}
