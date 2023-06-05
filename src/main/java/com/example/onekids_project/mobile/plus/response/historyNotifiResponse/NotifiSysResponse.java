package com.example.onekids_project.mobile.plus.response.historyNotifiResponse;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifiSysResponse extends IdResponse {

    private String content;

    private String title;

    private String avatar;

    private int numberFile;

    private int numberPicture;

    private String createdDate;

    private boolean userUnread;

}
