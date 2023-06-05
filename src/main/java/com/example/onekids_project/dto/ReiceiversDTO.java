package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReiceiversDTO extends IdDTO {

    private boolean userUnread;

    private LocalDateTime timeRead;

    private boolean isApproved;

    private boolean sendDel;

    private Long idUserReceiver;

//    private AppSend appSend;

//    private Long idSchool;
//
//    private Long idClass;
//
//    private Long idKids;

}
