package com.example.onekids_project.service.servicecustom.sms;

import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.service.dto.sms.SmsDTO;

import java.util.List;
import java.util.concurrent.Future;

public interface SmsService {
    /*
    Gửu tin nhắn cho một số điện thoại
    Sendby: userName người gửu tin nhắn
     */
    Future<SmsResultDTO> sendSms(long shoolId, String phone, String content);

    /*
    Gửu tin nhắn cho nhiều số điện thoại
    Sendby: userName người gửu tin nhắn
     */
    Future<List<SmsResultDTO>> sendSms(long shoolId, List<String> phones, String content);

    Future<SmsResultDTO> sendSmsOne(long shoolId, String phones, String content);

    Future<List<SmsResultDTO>> sendSmsMulti(long shoolId, List<String> phones, String content);

    SmsDTO getSmsDTOByShoolId(long shoolId);


}
