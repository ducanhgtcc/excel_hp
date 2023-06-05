package com.example.onekids_project.request.AppSend;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAppSendRequest extends IdRequest {

    private String schoolReply;

    private boolean schoolReplyDel;

    private String dataType;

    private boolean schoolUnread;
}
