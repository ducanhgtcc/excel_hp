package com.example.onekids_project.request.parentdiary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class
UpdateMedicineRequest extends IdRequest {

    private boolean confirmStatus;

    private boolean teacherUnread;

    private String teacherReply;

    private boolean teacherReplyDel;

    private String schoolReply;

    private LocalDateTime lastModifieDate;

    private boolean schoolReplyDel;

    private String defaultContentDel;

    private boolean schoolModifyStatus;

    private String dataType;

    private Long idConfirmType;

    private LocalDateTime confirmdate;

    private Long idSchoolReply;

    private LocalDateTime schoolTimeReply;

//    private LocalDateTime teacherTimeReply;
}
