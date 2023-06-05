package com.example.onekids_project.mobile.parent.response.absentletter;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AbsentLetterMobileResponse extends IdResponse {

    private String content;

    private String dateAbsent;

    private int replyNumber;

    private int pictureNumber;

    private LocalDateTime createdDate;

    private boolean confirmStatus;

    private boolean parentUnread;

    private int numberFile;
}
