package com.example.onekids_project.mobile.plus.request.historyNotifiRequest;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifiSysRequest extends PageNumberRequest {

    private String content;

    @Override
    public String toString() {
        return "NotifiSysRequest{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}

