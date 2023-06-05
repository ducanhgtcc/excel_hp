//package com.example.onekids_project.service.serviceimpl.firebaseimpl;
//
//import com.example.onekids_project.request.firebase.PushNotificationRequest;
//import com.example.onekids_project.service.servicecustom.firebase.PushNotificationService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.webjars.NotFoundException;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class PushNotificationServiceImpl implements PushNotificationService {
////    @Value("#{${app.notifications.defaults}}")
////    private Map<String, String> defaults;
//
//    private Logger logger = LoggerFactory.getLogger(PushNotificationServiceImpl.class);
//
//    @Autowired
//    private FCMServiceImpl fcmServiceImpl;
//
//
//
//    @Override
//    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
//        try {
//            fcmServiceImpl.sendMessageWithoutData(request);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new NotFoundException("error send without data");
//        }
//    }
//
//
//    @Override
//    public void sendPushNotificationToToken(PushNotificationRequest request) {
//        try {
//            fcmServiceImpl.sendMessageToToken(request);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new NotFoundException("error send token");
//        }
//    }
//
//    @Override
//    public void sendPushNotification(PushNotificationRequest request) {
//        try {
//            fcmServiceImpl.sendMessage(getSamplePayloadData(), request);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new NotFoundException("error send notification");
//        }
//    }
//
//
//
//
//
//    private Map<String, String> getSamplePayloadData() {
//        Map<String, String> pushData = new HashMap<>();
//        pushData.put("click_action", "FLUTTER_NOTIFICATION_CLICK");
//        pushData.put("route", "login1");
//        return pushData;
//
//    }
//    private Map<String, String> getSamplePayloadData1() {
//        Map<String, String> pushData = new HashMap<>();
//        pushData.put("click_action", "FLUTTER_NOTIFICATION_CLICK");
//        pushData.put("route", "login1");
//        pushData.put("idKid", "idKid");
//        //kid->idKid, teacher->idClass, plus->idSchool
//        //thông báo của hệ thống, route->home,
//        return pushData;
//
//    }
//
//
////    private PushNotificationRequest getSamplePushNotificationRequest() {
////        PushNotificationRequest request = new PushNotificationRequest(defaults.get("title"),
////                defaults.get("message"),
////                defaults.get("topic"));
////        return request;
////    }
//
//}
