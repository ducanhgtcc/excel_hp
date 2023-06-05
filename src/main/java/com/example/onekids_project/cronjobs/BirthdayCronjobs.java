package com.example.onekids_project.cronjobs;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.service.servicecustom.SmsSendService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@PropertySource(value = "cronjob.properties")
@Component
public class BirthdayCronjobs {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private PortBean portBean;

    /*
    gửi sinh nhật firebase, sms tự động 8h sáng hàng ngày của trường
     */
    @Scheduled(cron = "${birthday.school}")
    protected void executeInternal() throws FirebaseMessagingException, ExecutionException, InterruptedException {
        logger.info("--------Start send birthday auto--------");
        portBean.checkPortForCronjob();
        firebaseService.sendAutoFirebaseBirthday();
        logger.info("--------End send birthday auto--------");
    }
    /*
    gửi sinh nhật firebase, sms tự động 9h sáng hàng ngày của hệ thống
     */
    @Scheduled(cron = "${birthday.system}")
    protected void executeInternalSystem() throws FirebaseMessagingException, ExecutionException, InterruptedException {
        logger.info("--------Start send birthday system auto--------");
        portBean.checkPortForCronjob();
        firebaseService.sendAutoFirebaseBirthdaySystem();
        logger.info("--------End send birthday system auto--------");
    }
}
