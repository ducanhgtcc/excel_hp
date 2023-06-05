package com.example.onekids_project.response.schoolconfig;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class SysConfigShowResponse {
    private LocalTime timeSendCelebrate;

    private String qualityPicture;

    private String widthPicture;

    private boolean showTitleSms;

    private String titleContentSms;

}
