package com.example.onekids_project.response.parentdiary;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.common.FileResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageNewResponse extends IdResponse {
    private KidOtherResponse kids;

    private LocalDateTime createdDate;

    private String createdBy;

    private String messageContent;

    private List<FileResponse> messageParentAttachFileList;

    private boolean confirmStatus;

    private String teacherReply;

    private String schoolReply;

    private boolean teacherReplyDel;

    private boolean schoolReplyDel;

    private LocalDateTime teacherTimeReply;

    private LocalDateTime schoolTimeReply;

}
