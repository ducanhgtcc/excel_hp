package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.EvaluateSubject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ma_subject")
public class Subject extends BaseEntity<String> {

    @Column(nullable = false, unique = true)
    private String subjectCode;

    @Column(nullable = false)
    private String subjectName;

    @Column(length = 1000)
    private String note;

    @Column(nullable = false)
    private Long idSchool;

    @JsonManagedReference
    @OneToMany(mappedBy = "subject", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ExKidsSubject> exKidsSubjectList;

    @JsonManagedReference
    @OneToMany(mappedBy = "subject", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<EvaluateSubject> evaluateSubjectList;

    @JsonBackReference
    @ManyToMany(mappedBy = "subjectList", fetch = FetchType.LAZY)
    List<MaClass> maClassList;

    @JsonBackReference
    @ManyToMany(mappedBy = "subjectSet", fetch = FetchType.LAZY)
    Set<ExEmployeeClass> exEmployeeClassSet;
}
