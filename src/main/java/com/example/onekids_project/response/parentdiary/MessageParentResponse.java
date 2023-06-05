package com.example.onekids_project.response.parentdiary;

import com.example.onekids_project.entity.parent.MessageParentAttachFile;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.common.FileResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import com.example.onekids_project.security.payload.MaUserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageParentResponse extends IdResponse {
    private String messageContent;

    private boolean confirmStatus;

    private boolean teacherUnread;

    private String className;

    private LocalDateTime createdDate;

    private KidOtherResponse kids;

    private Long idClass;

    private Long idTeacherReply;

    private Long idSchoolReply;

    private List<FileResponse> messageParentAttachFileList;

}
