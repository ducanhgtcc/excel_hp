package com.example.onekids_project.firebase.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class PushNotificationDataRequest {
    private String title;

    private String body;

    Map<String, Object> dataMap;
}
