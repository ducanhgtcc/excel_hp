package com.example.onekids_project.mobile.plus.request.historyNotifiRequest;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchHistoryNotifiPlusRequest extends PageNumberRequest {

    private String appType;

    private String content;

    @Override
    public String toString() {
        return "SearchHistoryNotifiPlusRequest{" +
                "appType='" + appType + '\'' +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }
}

