package com.example.onekids_project.dto;

import com.example.onekids_project.entity.parent.AbsentLetterAttachFile;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AbsentLetterDTO {
    private String absentContent;

    private LocalDate fromDate;

    private LocalDate toDate;

    private boolean confirmStatus;

    private boolean teacherUnread;

    private String confirmType;

    private Long idConfirmType;

    private LocalDateTime confirmDate;

    private String teacherReply;

    private LocalDateTime createdDate;

    private LocalDateTime teacherTimeReply;

    private Boolean teacherReplyDel;

    private LocalDateTime schoolTimeReply;

    private Boolean schoolReplyDel;

    private String schoolReply;

    private String urlPicture;

    private KidOtherResponse kids;

    private String kidName;

    private String dateLeave;

    private Long idSchoolReply;

    private Long idTeacherReply;

    private String schoolFeedback;

    private List<AbsentLetterAttachFile> absentLetterAttachFileList;
}
