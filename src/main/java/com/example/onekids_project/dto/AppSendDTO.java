package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AppSendDTO extends IdDTO {
    private String sendType;

    private String sendTitle;

    private String sendContent;

    private String attachFile;

    private Integer countAttachFile;

    private String attachPicture;

    private Integer countAttachPicture;

    private LocalDateTime timeSend;

    private LocalDateTime createdDate;

    private Integer receivedNumber;

    private String createdBy;

    private boolean isApproved;

    private boolean sendDel;

    private List<Receivers> receiversList;

    private List<UrlFileAppSend> urlFileAppSendList;

}
