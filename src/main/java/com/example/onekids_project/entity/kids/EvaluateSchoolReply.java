package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "evaluate_school_reply")
public class EvaluateSchoolReply extends BaseEntity<String> {

    @Column(length = 5000)
    private String content;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean replyDel;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean parentUnread;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_evaluate_subject")
    private EvaluateSubject evaluateSubject;

}
