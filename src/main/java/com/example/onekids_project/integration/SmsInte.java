package com.example.onekids_project.integration;

import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.integration.exception.IntegrationException;

import java.util.List;

public interface SmsInte {
    public SmsResultDTO sendSms(String supplierCode, String serviceUrl, String username, String password, String phone, String content, int brandType, String brandname) throws IntegrationException;


    public List<SmsResultDTO> sendMutilSms(String supplierCode, String serviceUrl, String username, String password, String phones, String content, int brandType, String brandname) throws IntegrationException;
}
