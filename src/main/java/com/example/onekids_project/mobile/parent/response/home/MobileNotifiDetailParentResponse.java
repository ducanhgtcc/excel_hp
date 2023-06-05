package com.example.onekids_project.mobile.parent.response.home;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.MobileFile;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MobileNotifiDetailParentResponse {
    private String title;

    private String content;

    private LocalDateTime createdDate;

    private List<String> pictureList;

    private List<AttachFileMobileResponse> fileList;
}
