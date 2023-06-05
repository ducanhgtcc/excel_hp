package com.example.onekids_project.firebase.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PushNotificationTopicRequest {
    private String topic;

    private String title;

    private String body;


}
