package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SmsSendHistoryResponse extends IdResponse {

    private String titleContent;

    private String sendContent;

    private String sendType;

    private LocalDateTime timeAlarm;

    private String appType;

    private int smsNumber;

    private String serviceProvider;

    private boolean sent;

    private Long idSchool;

    private Long idUserSend;

    private String createdBy;

    private LocalDateTime createdDate;

    // Trang thai cho gui
    private String statusSend;

    private String nameUserSend;

    // số người nhận
    private int coutUserSent;

    // thành công
    private int coutSmsSuccess;

    // Thất bại
    private int coutSmsFail;

    @JsonIgnore
    private List<SmsReceivers> smsReceiversList;
}
