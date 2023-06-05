package com.example.onekids_project.master.response.notify;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotifyAdminResponse extends IdResponse {
    private LocalDateTime createdDate;

    private String createdBy;

    private String sendTitle;

    private String sendContent;

    private int fileNumber;
}
