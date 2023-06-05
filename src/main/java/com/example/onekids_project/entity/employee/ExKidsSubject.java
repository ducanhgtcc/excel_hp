package com.example.onekids_project.entity.employee;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntityId;
import com.example.onekids_project.entity.kids.Kids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ex_kids_subject")
public class ExKidsSubject extends BaseEntityId<String> {

    @Column(nullable = false, columnDefinition = "bit default true")
    private boolean isActive = AppConstant.APP_TRUE;

    @Column(nullable = false)
    private Long id_school;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subject", nullable = false)
    private Subject subject;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kid;
}
