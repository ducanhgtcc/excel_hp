package com.example.onekids_project.response.parentdiary;

import com.example.onekids_project.entity.parent.MedicineAttachFile;
import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidOtherResponse;
import com.example.onekids_project.security.payload.MaUserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MedicineResponse extends IdResponse {

    private LocalDate fromDate;

    private LocalDate toDate;

    private String medicineContent;

    private boolean confirmStatus;

    private boolean teacherUnread;

    private Long idConfirmType;

    private LocalDateTime createdDate;

    private Long idTeacherReply;

    private Long idConfirm;

    private LocalDateTime teacherTimeReply;

    private String schoolReply;

    private Long idSchoolReply;

    private String kidName;

    private KidOtherResponse kids;

    private Long idGrade;

    private Long idClass;

    private String schoolFeedback;

    private String teacherFeedback;

    private String dateDrink;

    private String diseaseName;

    // giaos vien phản hồi
    private String teacherReplyName;

    // nha truong phan hoi
    private String schoolReplyy;

    private String createdBy;

    private String nameConfirm;

    private int numberFile;

    private String clasName;

    private MaUserResponse maUserResponse;

    private List<MedicineAttachFile> medicineAttachFileList;
}
