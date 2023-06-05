package com.example.onekids_project.entity.sample;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "evaluate_sample")
public class EvaluateSample extends BaseEntity<String> {
//    EvaluateConstant: learn, eat, sleep, sanitary, healt, common
    @Column(nullable = false)
    private String evaluateType;

    @Column(nullable = false, length = 8000)
    private String evaluateContent;

    @Column(nullable = false)
    private Long idSchool;
}
