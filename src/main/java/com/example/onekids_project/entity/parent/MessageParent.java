package com.example.onekids_project.entity.parent;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ParentDairyConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Kids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_message_parent")
public class MessageParent extends BaseEntity<String> {

    @Column(nullable = false, length = 5000)
    private String messageContent;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean parentMessageDel;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean teacherUnread;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean parentUnread = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean confirmStatus;

    //apptype? teacher or plus
    private String confirmType;

    private Long idConfirm;

    //ParentDairyConstant.CONTENT_CONFIRM_SCHOOL
    private String confirmContent;

    private LocalDateTime confirmDate;

    private Long idTeacherReply;

    @Column(length = 2000)
    private String teacherReply;

    private LocalDateTime teacherTimeReply;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean teacherReplyDel;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean teacherModifyStatus;

    private String defaultContentDel = ParentDairyConstant.CONTENT_DEL;

    @Column(length = 2000)
    private String schoolReply;

    private Long idSchoolReply;

    private LocalDateTime schoolTimeReply;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean schoolReplyDel;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean schoolModifyStatus;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private Long idGrade;

    @Column(nullable = false)
    private Long idClass;

    @JsonManagedReference
    @OneToMany(mappedBy = "messageParent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<MessageParentAttachFile> messageParentAttachFileList;

    // N- 1 kids
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kids;
}
