package com.example.onekids_project.response.parentdiary;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MobileMessageParentResponse {
    private String messageContent;

    private boolean parentMessageDel;

    private boolean confirmStatus;

    private boolean teacherUnread;

    private boolean parentUnread;

    private String confirmType;

    private Long idConfirmType;

    private String defaultContentSchool;

    private String defaultContentTeacher;

    private LocalDateTime confirmDate;

    private Long idTeacherReply;

    private String teacherReply;

    private LocalDateTime teacherTimeReply;

    private Boolean teacherReplyDel;

    private boolean teacherModifyStatus;

    private String defaultContentDel;

    private String schoolReply;

    private Long idSchoolReply;

    private LocalDateTime schoolTimeReply;

    private Boolean schoolReplyDel;

    private boolean schoolModifyStatus;

    private int countReply;

    private int countPicture;

    private String urlPicture;

}
