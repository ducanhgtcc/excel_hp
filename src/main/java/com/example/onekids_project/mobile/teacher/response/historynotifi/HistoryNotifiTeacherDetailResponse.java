package com.example.onekids_project.mobile.teacher.response.historynotifi;

import com.example.onekids_project.mobile.response.ListFileNotifi;
import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HistoryNotifiTeacherDetailResponse extends IdResponse {

    private String fullName;

    private String avatarkid;

    private String className;

    private int coutNumberReceivers;

    private String statusSend;

    private String timeSend;

    private String sendTitle;

    private String sendContent;

    private List<String> pictureList;

    private List<ListFileNotifi> fileList;
}
