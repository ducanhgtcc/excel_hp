package com.example.onekids_project.response.notifihistory;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReiceiversResponse extends IdResponse {

    private boolean userUnread;

    private LocalDateTime timeRead;

    private boolean isApproved;

    private boolean sendDel;

    private Long idUserReceiver;

}
