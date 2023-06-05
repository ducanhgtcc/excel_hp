package com.example.onekids_project.request.firebase;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PushNotificationRequest {
    private String title;

    private String message;

    private String topic;

    private String token;
}
