package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntityNoAuditing;
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
@Table(name = "ma_evaluate_date")
public class EvaluateDate extends BaseEntityNoAuditing<String> {
    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private Long idGrade;

    @Column(nullable = false)
    private Long idClass;

    @Column(nullable = false)
    private LocalDate date;

    //phụ huynh đọc nhận xét của giáo viên hoặc nhà trường
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean parentRead;

    @Column(nullable = false)
    private boolean approved;

    private Long learnIdCreated;

    private String learnCreatedBy;

    private LocalDateTime learnDatetime;

    @Column(length = 5000)
    private String learnContent = "";


    private Long eatIdCreated;

    private String eatCreatedBy;

    private LocalDateTime eatDatetime;

    @Column(columnDefinition = "TEXT")
    private String eatContent = "";


    private Long sleepIdCreated;

    private String sleepCreatedBy;

    private LocalDateTime sleepDatetime;

    @Column(columnDefinition = "TEXT")
    private String sleepContent = "";


    private Long sanitaryIdCreated;

    private String sanitaryCreatedBy;

    private LocalDateTime sanitaryDatetime;

    @Column(columnDefinition = "TEXT")
    private String sanitaryContent = "";


    private Long healtIdCreated;

    private String healtCreatedBy;

    private LocalDateTime healtDatetime;

    @Column(columnDefinition = "TEXT")
    private String healtContent = "";


    private Long commonIdCreated;

    private String commonCreatedBy;

    private LocalDateTime commonDatetime;

    @Column(columnDefinition = "TEXT")
    private String commonContent = "";

    @JsonManagedReference
    @OneToMany(mappedBy = "evaluateDate", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EvaluateAttachFile> evaluateAttachFileList;

    private Long parentReplyIdCreated;

    private String parentReplyCreatedBy;

    private LocalDateTime parentReplyDatetime;

    private boolean parentReplyDel;

    //true là đã chỉnh sửa, false là chưa chỉnh sửa
    private boolean parentReplyModified;

    @Column(columnDefinition = "TEXT")
    private String parentReplyContent = "";

    //giáo viên hoặc nhà trường đọc phản hồi của phụ huynh, true là đã đọc
    private boolean schoolReadReply;


    private Long teacherReplyIdCreated;

    private String teacherReplyCreatedBy;

    private LocalDateTime teacherReplyDatetime;

    private boolean teacherReplyDel;

    //true là đã chỉnh sửa, false là chưa chỉnh sửa
    private boolean teacherReplyModified;

    @Column(columnDefinition = "TEXT")
    private String teacherReplyContent = "";


    private Long schoolReplyIdCreated;

    private String schoolReplyCreatedBy;

    private LocalDateTime schoolReplyDatetime;

    private boolean schoolReplyDel;

    //true là đã chỉnh sửa, false là chưa chỉnh sửa
    private boolean schoolReplyModified;

    @Column(columnDefinition = "TEXT")
    private String schoolReplyContent = "";


//    //phụ huynh đọc phản hồi của giáo viên hoặc nhà trường
//    private boolean parentReadReply;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kids;

}
