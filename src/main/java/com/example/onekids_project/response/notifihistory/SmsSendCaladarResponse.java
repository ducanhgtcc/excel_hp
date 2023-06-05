package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsSendCaladarResponse extends IdResponse {

    private String name;

    private String phone;

    private String content;

}
