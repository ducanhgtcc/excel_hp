package com.example.onekids_project.request.parentdiary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateMessageParentRequest extends IdRequest {

    private boolean teacherUnread;

    private String teacherReply;

    private boolean teacherReplyDel;

    private String schoolReply;

    private Long idSchoolReply;

    private boolean schoolReplyDel;

    private String dataType;

//    private Long idConfirm;

    private LocalDateTime schoolTimeReply;
}
