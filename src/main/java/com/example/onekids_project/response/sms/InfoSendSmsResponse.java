package com.example.onekids_project.response.sms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoSendSmsResponse {
    private String phone;
    private String errCode;
    private String errMsg;
    private boolean success;

}
