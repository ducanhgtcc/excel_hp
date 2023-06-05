//package com.example.onekids_project.cronjobs;
//
//import com.example.onekids_project.firebase.servicecustom.FirebaseService;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@PropertySource(value = "cronjob.properties")
//@Component
//public class FirebaseCronjobs {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//    @Autowired
//    private FirebaseService firebaseService;
//
//    /*
//    gửi lại firebase lỗi, 60p quét 1 lần, chạy từ 8h->21h
//     */
//    @Scheduled(cron = "${firebase.sendagain}")
//    protected void executeInternal() throws FirebaseMessagingException {
//        logger.info("--------Start send again firebase fail auto--------");
//        firebaseService.sendFirebaseFail();
//        logger.info("--------End send again firebase fail auto--------");
//    }
//}
