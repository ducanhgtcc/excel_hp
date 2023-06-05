package com.example.onekids_project.master.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsSchoolResponse {

    private String smsDate;

    private long numberSms;

    private String createBy;

    private String content;
}
