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
public class AbsentNewResponse extends IdResponse {
    private KidOtherResponse kids;

    private LocalDateTime createdDate;

    private String createdBy;

    private String absentContent;

    private LocalDate fromDate;

    private LocalDate toDate;

    private List<FileResponse> absentLetterAttachFileList;

    private boolean confirmStatus;

    private String teacherReply;

    private String schoolReply;

    private boolean teacherReplyDel;

    private boolean schoolReplyDel;

    private LocalDateTime teacherTimeReply;

    private LocalDateTime schoolTimeReply;

}
