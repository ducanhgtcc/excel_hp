package com.example.onekids_project.entity.kids;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.parent.AbsentLetterAttachFile;
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
@Table(name = "ma_absent_letter")
public class AbsentLetter extends BaseEntity<String> {

    @Column(length = 5000, nullable = false)
    private String absentContent;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(columnDefinition = "bit default 0")
    private boolean parentAbsentDel;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean confirmStatus;

    //giáo viên hoặc nhà tường đọc, khi click chi tiết thì set đã xem(true)
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean teacherUnread;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean parentUnread = true;

    // plus or teacher
    private String confirmType;

    private Long idConfirmType;

    //CONTENT_CONFIRM_SCHOOL
    private String confirmContent;

    private LocalDateTime confirmDate;

    private Long idTeacherReply;

    @Column(length = 2000)
    private String teacherReply;

    //thời gian sửa phản hồi, sau này bổ sung thêm thời gian tạo để sắp xếp
    private LocalDateTime teacherTimeReply;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean teacherModifyStatus;

    private boolean teacherReplyDel;

    //thời gian sửa phản hồi, sau này bổ sung thêm thời gian tạo để sắp xếp
    private LocalDateTime schoolTimeReply;

    private boolean schoolReplyDel;

    @Column(length = 2000)
    private String schoolReply;

    private Long idSchoolReply;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean schoolModifyStatus;

    private String defaultContentDel= AppConstant.DEFAULT_CONTENT_DEL;

    private boolean expired;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private Long idGrade;

    @Column(nullable = false)
    private Long idClass;

    @JsonManagedReference
    @OneToMany(mappedBy = "absentLetter", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AbsentLetterAttachFile> absentLetterAttachFileList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kids;

    // 1-n
    @JsonManagedReference
    @OneToMany(mappedBy = "absentLetter", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<AbsentDate> absentDateList;

}
