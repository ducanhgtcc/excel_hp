package com.example.onekids_project.mobile.teacher.response.historynotifi;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HistoryNotifiTeacherResponse extends IdResponse {

    private String fullName;

    private String avatar;

    private String sendContent;

    private boolean isApprove;

    private int coutPicture;

    private int coutFile;

    private String createdDate;
}
