package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EvaluateTeacherReplyResponse extends IdResponse {
//    private String itemName = "Giáo viên phản hồi";

    private String content;

    private boolean replyDel;

    private String lastModifieBy;

    private LocalDateTime lastModifieDate;

//    private boolean parentUnread;
}
