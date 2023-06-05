package com.example.onekids_project.mobile.parent.response.message;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageParentMobileResponse extends IdResponse {

    private String content;

    private int replyNumber;

    private int pictureNumber;

    private LocalDateTime createdDate;

    private boolean confirmStatus;

    private boolean parentUnread;
}
