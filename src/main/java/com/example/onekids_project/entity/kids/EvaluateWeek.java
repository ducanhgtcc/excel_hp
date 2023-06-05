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
@Table(name = "ma_evaluate_week")
public class EvaluateWeek extends BaseEntityNoAuditing<String> {

    private int week;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private Long idGrade;

    @Column(nullable = false)
    private Long idClass;

    //true là đã đọc, false là chưa đọc
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean parentRead;

    @Column(nullable = false)
    private boolean approved;

    @Column(columnDefinition = "TEXT")
    private String content = "";

    @JsonManagedReference
    @OneToMany(mappedBy = "evaluateWeek", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EvaluateWeekFile> evaluateWeekFileList;


    private Long parentReplyIdCreated;

    private String parentReplyCreatedBy;

    private LocalDateTime parentReplyDatetime;

    private boolean parentReplyDel;

    private boolean schoolReadReply;

    //true là đã chỉnh sửa, false là chưa chỉnh sửa
    private boolean parentReplyModified;

    @Column(columnDefinition = "TEXT")
    private String parentReplyContent = "";


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


//    private boolean parentReadReply;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kids;

}
