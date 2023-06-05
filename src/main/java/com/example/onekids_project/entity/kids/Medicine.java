package com.example.onekids_project.entity.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.parent.MedicineAttachFile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_medicine")
public class Medicine extends BaseEntity<String> {

    @Column(nullable = false)
    private String diseaseName;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(length = 5000)
    private String medicineContent;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean parentMedicineDel;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean confirmStatus;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean teacherUnread;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean parentUnread = AppConstant.APP_TRUE;

    // plus or teacher
    private String confirmType;

    private Long idConfirmType;

    //CONTENT_CONFIRM_SCHOOL
    private String confirmContent;

    private LocalDateTime confirmdate;

    private Long idTeacherReply;

    @Column(length = 2000)
    private String teacherReply;

    private LocalDateTime teacherTimeReply;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean teacherModifyStatus;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean teacherReplyDel;

    @Column(length = 2000)
    private String schoolReply;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean schoolReplyDel;

    private Long idSchoolReply;

    private LocalDateTime schoolTimeReply;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean schoolModifyStatus;

    private String defaultContentDel;

//    @Column(columnDefinition = "int default 0")
//    private int countReply;
//
//    @Column(columnDefinition = "int default 0")
//    private int countPicture;
//
//    private String urlPicture;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private Long idGrade;

    @Column(nullable = false)
    private Long idClass;

    @JsonManagedReference
    @OneToMany(mappedBy = "medicine", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MedicineAttachFile> medicineAttachFileList;

    // n-1 kids
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idKids", nullable = false)
    private Kids kids;

}
