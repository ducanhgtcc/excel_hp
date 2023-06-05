package com.example.onekids_project.mobile.plus.request.historyNotifiRequest;

import com.example.onekids_project.mobile.request.PageNumberRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailUserRequest extends PageNumberRequest {

    private String content;

    @Override
    public String toString() {
        return "DetailUserRequest{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}

