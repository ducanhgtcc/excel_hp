package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.mobile.response.ListFileNotifi;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotifiSysDetailResponse {

    private String content;

    private String title;

    private String createdDate;

    private List<String> pictureList;

    private List<ListFileNotifi> fileList;
}
