package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReiceiversResponeHistoru extends IdResponse {

    private String createdBy;

    private String phone;

    private String type;

    private String className;

    private boolean isApproved;

    private String nameReiceiver;

    private boolean sendDel;

    private List<UrlFileAppSend> urlFileAppSendList;
}
