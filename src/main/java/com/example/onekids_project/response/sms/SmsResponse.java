package com.example.onekids_project.response.sms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsResponse {

    private String title;
    private boolean showTitle;
    private boolean smsMore;
    private boolean checkActiveSms;
    private Long smsRemain;
}
