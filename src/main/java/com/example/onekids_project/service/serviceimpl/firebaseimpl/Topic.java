//package com.example.onekids_project.service.serviceimpl.firebaseimpl;
//
//import com.google.firebase.messaging.*;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//public class Topic {
//    public void subscribeToTopic() throws FirebaseMessagingException {
//        String topic = "mytopic123";
//        // [START subscribe]
//        // These registration tokens come from the client FCM SDKs.
//        List<String> registrationTokens = Arrays.asList(
//                "ftOmE4327O8:APA91bG3z9P4-G7OEfE3INv3CdmYSMhRa2doL2Kcu7NiFc1O-eRM4gwAU6RMlBiQBRBZPRtAWmcN2N8-rEznXnnCVCJZAldrjXSl23frLtlVg4EiCz1Ge6ROFajBr5ybjS1f4z-qjW9T",
//                "cdGJ9rq1U0Y:APA91bFGtZsOUBk8caNnooeG92-8PsQP83XUkrV5u4M3Ru-sOdKKsDYKqymRSMcF0j32NlUYofklr-VmuQkCVbWzIJMrwd5fhRnQw-dSxtJCOisxkwuRMOsS69Z_iCBsbvQMJ0CNL4nI"
//                // ...
//        );
//        // Subscribe the devices corresponding to the registration tokens to the
//        // topic.
//        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
//                registrationTokens, topic);
//        // See the TopicManagementResponse reference documentation
//        // for the contents of response.
//        System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");
//        // [END subscribe]
//    }
//
//    public void unsubscribeFromTopic() throws FirebaseMessagingException {
//        String topic = "highScores";
//        // [START unsubscribe]
//        // These registration tokens come from the client FCM SDKs.
//        List<String> registrationTokens = Arrays.asList(
//                "YOUR_REGISTRATION_TOKEN_1",
//                // ...
//                "YOUR_REGISTRATION_TOKEN_n"
//        );
//
//        // Unsubscribe the devices corresponding to the registration tokens from
//        // the topic.
//        TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
//                registrationTokens, topic);
//        // See the TopicManagementResponse reference documentation
//        // for the contents of response.
//        System.out.println(response.getSuccessCount() + " tokens were unsubscribed successfully");
//        // [END unsubscribe]
//    }
//
//    public void sendMulticast() throws FirebaseMessagingException {
//        // [START send_multicast]
//        // Create a list containing up to 100 registration tokens.
//        // These registration tokens come from the client FCM SDKs.
//        List<String> registrationTokens = Arrays.asList(
//                "ftOmE4327O8:APA91bG3z9P4-G7OEfE3INv3CdmYSMhRa2doL2Kcu7NiFc1O-eRM4gwAU6RMlBiQBRBZPRtAWmcN2N8-rEznXnnCVCJZAldrjXSl23frLtlVg4EiCz1Ge6ROFajBr5ybjS1f4z-qjW9T"
//        );
//
//        Notification notify = Notification.builder().setTitle("title aaa").setBody("body 123").build();
//        MulticastMessage message = MulticastMessage.builder()
//                .setNotification(notify)
//                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
//                .putData("route", "login1")
//                .addAllTokens(registrationTokens)
//                .build();
//        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
//        // See the BatchResponse reference documentation
//        // for the contents of response.
//        System.out.println(response.getSuccessCount() + " messages were sent successfully");
//        // [END send_multicast]
//    }
//
//    public void sendMulticastAndHandleErrors() throws FirebaseMessagingException {
//        // [START send_multicast_error]
//        // These registration tokens come from the client FCM SDKs.
//        List<String> registrationTokens = Arrays.asList(
//                "YOUR_REGISTRATION_TOKEN_1",
//                // ...
//                "YOUR_REGISTRATION_TOKEN_n"
//        );
//        Notification notify = Notification.builder().setTitle("title aaa").setBody("body 123").build();
//        MulticastMessage message = MulticastMessage.builder()
//                .setNotification(notify)
//                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
//                .putData("route", "login1")
//                .addAllTokens(registrationTokens)
//                .build();
//        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
//        if (response.getFailureCount() > 0) {
//            List<SendResponse> responses = response.getResponses();
//            List<String> failedTokens = new ArrayList<>();
//            for (int i = 0; i < responses.size(); i++) {
//                if (!responses.get(i).isSuccessful()) {
//                    // The order of responses corresponds to the order of the registration tokens.
//                    failedTokens.add(registrationTokens.get(i));
//                }
//            }
//
//            System.out.println("List of tokens that caused failures: " + failedTokens);
//        }
//        // [END send_multicast_error]
//    }
//
//    public void sendToToken() throws FirebaseMessagingException {
//        // [START send_to_token]
//        // This registration token comes from the client FCM SDKs.
//        String registrationToken = "YOUR_REGISTRATION_TOKEN";
//
//        Notification notify = Notification.builder().setTitle("title aaa").setBody("body 123").build();
//        // See documentation on defining a message payload.
//        Message message = Message.builder()
//                .setNotification(notify)
//                .putData("score", "850")
//                .putData("time", "2:45")
//                .setToken(registrationToken)
//                .build();
//
//        // Send a message to the device corresponding to the provided
//        // registration token.
//        String response = FirebaseMessaging.getInstance().send(message);
//        // Response is a message ID string.
//        System.out.println("Successfully sent message: " + response);
//        // [END send_to_token]
//    }
//}
