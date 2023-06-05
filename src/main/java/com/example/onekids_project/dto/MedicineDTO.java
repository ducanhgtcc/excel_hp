package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.entity.parent.MedicineAttachFile;
import com.example.onekids_project.response.kids.KidOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MedicineDTO extends IdDTO {

    private LocalDate fromDate;

    private LocalDate toDate;

    private String medicineContent;

    private boolean confirmStatus;

    private boolean teacherUnread;

    private boolean parentUnread;

    private Long idConfirmType;

    private LocalDateTime createdDate;

    private LocalDateTime confirmdate;

    private Long idTeacherReply;

    private String teacherReply;

    private Boolean teacherReplyDel;

    private String schoolReply;

    private Long idSchoolReply;

    private LocalDateTime schoolTimeReply;

    private Boolean schoolReplyDel;

    private String kidName;

    private String urlPicture;

    //    private MaClass maClass;
    private KidOtherResponse kids;

    private Long idGrade;

    private String schoolFeedback;

    private String teacherFeedback;

    private String diseaseName;

    private String dateDrink;

    private List<MedicineAttachFile> medicineAttachFileList;

}
