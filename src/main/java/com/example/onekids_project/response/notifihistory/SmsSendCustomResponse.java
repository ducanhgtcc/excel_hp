package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.entity.user.SmsReceiversCustom;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SmsSendCustomResponse extends IdResponse {

    private String sendTitle;

    private String sendType;

    private Long idSchool;

    private String appType;

    private String createdDate;

    private Integer receivedCount;

    private Integer coutSms;

    private String createdBy;

    private Integer coutSmsSuccess;

    private String serviceProvider;

    private List<SmsReceiversCustom> smsReceiversCustomList;

}
