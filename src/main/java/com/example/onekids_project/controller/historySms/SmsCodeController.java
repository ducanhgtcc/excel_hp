package com.example.onekids_project.controller.historySms;

import com.example.onekids_project.service.servicecustom.SmsReiceriversService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/reiceivers")
public class SmsCodeController {
    private static final Logger logger = LoggerFactory.getLogger(SmsCodeController.class);
    @Autowired
    private SmsReiceriversService smsReiceriversService;

}
