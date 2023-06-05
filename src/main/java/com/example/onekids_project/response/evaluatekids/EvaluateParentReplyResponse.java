package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EvaluateParentReplyResponse extends IdResponse {
//    private String itemName = "Phụ huynh phản hồi";

    private String content;

    private boolean schoolUnread;

    private boolean replyDel;

    private String lastModifieBy;

    private LocalDateTime lastModifieDate;

//    private boolean schoolUnread;
}
