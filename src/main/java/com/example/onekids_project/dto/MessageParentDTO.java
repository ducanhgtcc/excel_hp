package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.entity.parent.MessageParentAttachFile;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MessageParentDTO extends IdDTO {

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

    private LocalDateTime createdDate;

    private Long idTeacherReply;

    private String teacherReply;

    private String teacherFeedback;

    private LocalDateTime teacherTimeReply;

    private Boolean teacherReplyDel;

    private boolean teacherModifyStatus;

    private String defaultContentDel;

    private Long idSchoolReply;

    private String schoolReply;

    private LocalDateTime schoolTimeReply;

    private Boolean schoolReplyDel;

    private boolean schoolModifyStatus;

    private int countReply;

    private String kidName;

    private String schoolFeedback;

    private int countPicture;

    private String urlPicture;

    private Long idSchool;

    private Long idGrade;

    private KidOtherResponse kids;

    private List<MessageParentAttachFile> messageParentAttachFileList;
}
