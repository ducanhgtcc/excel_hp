package com.example.onekids_project.mobile.parent.response.medicine;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MedicineMobileResponse extends IdResponse {

    private String content;

    private String disease_name;

//    private String fromDate;
//
//    private String toDate;

    private String dateMedicine;

    private int replyNumber;

    private int pictureNumber;

    private LocalDateTime createdDate;

    private boolean confirmStatus;

    private boolean parentUnread;

}
