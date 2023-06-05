package com.example.onekids_project.response.appsend;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReceiversResponse extends IdResponse {
    private boolean user_unread;

    private LocalDateTime time_read;

    private boolean isApproved;

    private boolean sendDel;

    private boolean userUnread;

    private Long idUserReceiver;

    private Long idSchool;

    private Long idClass;

    private Long idKids;

}
