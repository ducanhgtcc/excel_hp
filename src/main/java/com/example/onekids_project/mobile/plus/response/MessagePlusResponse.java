package com.example.onekids_project.mobile.plus.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessagePlusResponse extends IdResponse {

    private String kidName;

    private String content;

    private String createdDate;

    private int replyNumber;

    private int pictureNumber;

    private String avatar;

    private boolean confirmStatus;

    private boolean schoolUnread;

}
