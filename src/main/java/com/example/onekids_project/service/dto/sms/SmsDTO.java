package com.example.onekids_project.service.dto.sms;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SmsDTO {

    private String brandName;

    private int brandType;

    private String supplierCode;

    private String serviceUrl;

    private String userName;

    private String passowrd;

    private LocalDateTime currLocalDateTime;

}
