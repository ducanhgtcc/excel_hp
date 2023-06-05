package com.example.onekids_project.firebase.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NotifyRequest {
    private String title;

    private String body;

    public NotifyRequest() {
    }
}
