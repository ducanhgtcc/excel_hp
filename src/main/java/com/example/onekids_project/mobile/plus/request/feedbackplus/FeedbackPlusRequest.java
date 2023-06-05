package com.example.onekids_project.mobile.plus.request.feedbackplus;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackPlusRequest extends PageNumberRequest {

    private String date;

    private Boolean confirmStatus;

    private String content;

    private String typeSend;

    @Override
    public String toString() {
        return "FeedbackPlusRequest{" +
                "date='" + date + '\'' +
                ", confirmStatus=" + confirmStatus +
                ", content='" + content + '\'' +
                ", typeSend='" + typeSend + '\'' +
                "} " + super.toString();
    }
}
