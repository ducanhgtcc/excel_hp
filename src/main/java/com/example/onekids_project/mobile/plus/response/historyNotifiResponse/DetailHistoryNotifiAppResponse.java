package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.mobile.response.ListFileNotifi;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailHistoryNotifiAppResponse extends IdResponse {

    private String fullName;

    private String avatar;

    private String className;

    private String createdName;

    private String phone;

    private String createdDate;

    private String title;

    private String content;

    private String statusSend;

    private int coutNumberReceivers;

    private List<String> pictureList;

    private List<ListFileNotifi> fileList;
}
