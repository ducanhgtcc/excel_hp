package com.example.onekids_project.firebase.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PushNotificationResponse {
    private String application = "ONEKIDS APPLICAION";

    private int status;

    private String message;

    public PushNotificationResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
