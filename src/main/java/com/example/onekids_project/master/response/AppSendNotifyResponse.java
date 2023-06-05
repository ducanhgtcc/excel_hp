package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AppSendNotifyResponse extends IdResponse {
    private String sendTitle;

    private String sendContent;

    private LocalDateTime timeSend;

    private LocalDateTime createdDate;

    private String createdBy;

    private Long idAgent;

    private Long idSchool;

    private Long idClass;

    private String[] appTypeArr;

    private int numberFile;

    private List<FileAttachAppSendResponse> fileAttachAppSendResponseList;
}
