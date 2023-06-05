package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.entity.user.SmsReceivers;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SmsSendDTO extends IdDTO {

    private String titleContent;

    private String sendContent;

    private LocalDateTime timeAlarm;

    private String appType;

    private int smsNumber;

    private boolean sent;

    private String test;

//    @JsonIgnore
    private List<SmsReceivers> smsReceiversList;

}
