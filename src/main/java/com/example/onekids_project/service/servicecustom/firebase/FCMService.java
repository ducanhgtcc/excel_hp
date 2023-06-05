//package com.example.onekids_project.service.servicecustom.firebase;
//
//import com.example.onekids_project.request.firebase.PushNotificationRequest;
//import com.google.firebase.messaging.FirebaseMessagingException;
//
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//public interface FCMService {
//    /**
//     * for topic
//     * @param request
//     */
//    void sendMessageWithoutData(PushNotificationRequest request) throws InterruptedException, ExecutionException;
//
//    /**
//     * for token
//     * @param request
//     */
//    void sendMessageToToken(PushNotificationRequest request) throws InterruptedException, ExecutionException;
//
//    /**
//     * for data
//     * @param data
//     * @param request
//     */
//    void sendMessage(Map<String, String> data, PushNotificationRequest request) throws InterruptedException, ExecutionException;
//
//
////    void subscribeToTopic() throws FirebaseMessagingException;
//}
