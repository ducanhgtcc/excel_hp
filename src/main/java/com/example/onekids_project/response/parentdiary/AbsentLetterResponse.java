package com.example.onekids_project.response.parentdiary;

import com.example.onekids_project.entity.parent.AbsentLetterAttachFile;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AbsentLetterResponse extends IdResponse {
    private String absentContent;

    private LocalDate fromDate;

    private LocalDate toDate;

    private boolean confirmStatus;

    private boolean teacherUnread;

    private String confirmType;

    private Long idConfirmType;

    private String confirmName;

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

    private Long idClass;

    private String className;

    private Long idTeacherReply;

    private String schoolFeedback;

    private String statusfirst;

    // giaos vien phản hồi
    private String teacherReplyName;

    // nha truong phan hoi
    private String schoolReplyy;

    private String createdBy;

    private int numberFile;

    private String nameConfirm;

    //true->đơn đã tạo quá hạn cho phép
    private boolean expired;

    private List<AbsentLetterAttachFile> absentLetterAttachFileList;
}
