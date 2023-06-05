package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.Subject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ma_evaluate_subject")
public class EvaluateSubject extends BaseEntity<String> {

    private Long idKids;

    private Long idSchool;

    private int year;

    @Column(nullable = true, length = 5000)
    private String evaluateContent;

    @Column(nullable = false)
    private LocalDate evaluateDate;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean parentUnread;

    @Column(nullable = true, length = 1000)
    private String listUrlFile;

    @JsonManagedReference
    @OneToOne(mappedBy = "evaluateSubject", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private EvaluateTeacherReply evaluateTeacherReply;

    @JsonManagedReference
    @OneToOne(mappedBy = "evaluateSubject", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private EvaluateSchoolReply evaluateSchoolReply;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subject", nullable = false)
    private Subject subject;

}
