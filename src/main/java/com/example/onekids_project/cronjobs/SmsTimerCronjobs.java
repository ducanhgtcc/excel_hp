//package com.example.onekids_project.cronjobs;
//
//import com.example.onekids_project.bean.PortBean;
//import com.example.onekids_project.service.servicecustom.SmsSendService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@PropertySource(value = "cronjob.properties")
//@Component
//public class SmsTimerCronjobs {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//    @Autowired
//    private SmsSendService smsSendService;
//
//    @Autowired
//    private PortBean portBean;
//
//    /**
//     * gửi tự động các tin nhắn sms hẹn giờ
//     */
//    @Scheduled(cron = "${sms.notify}")
//    protected void executeInternal() {
//        logger.info("---------start auto create send sms---------");
//        portBean.checkPortForCronjob();
//        smsSendService.findSendSms();
//        logger.info("---------end auto create send sms---------");
//    }
//}
