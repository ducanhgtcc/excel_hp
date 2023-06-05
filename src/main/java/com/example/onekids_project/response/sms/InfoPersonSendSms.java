package com.example.onekids_project.response.sms;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoPersonSendSms extends IdResponse {
    private String fullName;
}
