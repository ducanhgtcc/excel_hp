package com.example.onekids_project.service.servicecustom.common;

import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.response.sms.SmsResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public interface FindSmsService {
    SmsResponse findSms(UserPrincipal principal);

    SmsConvertResponse convertSms(UserPrincipal principal, String contentSms);
}
