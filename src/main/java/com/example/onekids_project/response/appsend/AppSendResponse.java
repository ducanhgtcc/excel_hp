package com.example.onekids_project.response.appsend;

import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AppSendResponse extends IdResponse {

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

    private int numberFile;
    // số người nhận
    private int coutUserSent;

    private boolean userUnread;

    // check con cai nao chưa duyetj không
    private boolean ApproveRe;

    @JsonBackReference
    private List<ReceiversResponse> receiversList;

    @JsonBackReference
    private List<UrlFileAppSend> urlFileAppSendList;
}
