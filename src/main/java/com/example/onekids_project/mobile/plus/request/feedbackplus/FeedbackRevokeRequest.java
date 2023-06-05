package com.example.onekids_project.mobile.plus.request.feedbackplus;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRevokeRequest extends IdRequest {

    private String keyType;

    @Override
    public String toString() {
        return "FeedbackRevokeRequest{" +
                "keyType='" + keyType + '\'' +
                "} " + super.toString();
    }
}
