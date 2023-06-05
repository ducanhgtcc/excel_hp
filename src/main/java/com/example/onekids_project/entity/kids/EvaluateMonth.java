package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntityNoAuditing;
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
@Table(name = "ma_evaluate_month")
public class EvaluateMonth extends BaseEntityNoAuditing<String> {

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private Long idGrade;

    @Column(nullable = false)
    private Long idClass;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean parentRead;

    @Column(nullable = false)
    private boolean approved;

    @Column(columnDefinition = "TEXT")
    private String content="";

    @JsonManagedReference
    @OneToMany(mappedBy = "evaluateMonth", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EvaluateMonthFile> evaluateMonthFileList;


    private Long parentReplyIdCreated;

    private String parentReplyCreatedBy;

    private LocalDateTime parentReplyDatetime;

    private boolean parentReplyDel;

    private boolean schoolReadReply;

    private boolean parentReplyModified;

    @Column(columnDefinition = "TEXT")
    private String parentReplyContent="";


    private Long teacherReplyIdCreated;

    private String teacherReplyCreatedBy;

    private LocalDateTime teacherReplyDatetime;

    private boolean teacherReplyDel;

    private boolean teacherReplyModified;

    @Column(columnDefinition = "TEXT")
    private String teacherReplyContent="";


    private Long schoolReplyIdCreated;

    private String schoolReplyCreatedBy;

    private LocalDateTime schoolReplyDatetime;

    private boolean schoolReplyDel;

    private boolean schoolReplyModified;

    @Column(columnDefinition = "TEXT")
    private String schoolReplyContent="";


//    private boolean parentReadReply;

    //n-1 kids
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kids;

}
