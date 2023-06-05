package com.example.onekids_project.master.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReceiversResponse extends IdResponse {

    private String fullName;
    private String phone;
    private String appType;
    private String userUnread;
    private LocalDateTime timeRead;
    private String schoolName;
    private Boolean unRead;
    private Boolean sendDel;
    private Boolean isApproved;
    private String className;
}
