package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.response.appsend.ReceiversResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ReceiverDTO extends IdDTO {

    private boolean userUnread;

    private LocalDateTime timeRead;

    private boolean isApproved;

    private boolean sendDel;

    private Long idUserReceiver;

    @JsonBackReference
    private List<ReceiversResponse> receiversResponseList;
}
