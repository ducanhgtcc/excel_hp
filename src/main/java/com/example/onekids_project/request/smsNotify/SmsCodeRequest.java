package com.example.onekids_project.request.smsNotify;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsCodeRequest {

    private String codeError;

    private double serviceProvider;

    private String description;
}
