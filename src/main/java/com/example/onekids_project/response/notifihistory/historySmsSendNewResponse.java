package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class historySmsSendNewResponse extends IdResponse {

    private String titleContent;

    private String sendContent;

    private String createdName;

    private String sendType;

    private LocalDateTime timeAlarm;

    private String appType;

    private int smsSendTotal;

    private String serviceProvider;

    private Long id_user_send;

    private boolean sent;

    private Long id_school;

    private int coutSuccess;

    private int coutFail;

    private int coutAll;

//    private List<SmsReceivers> smsReceiversList;

}
