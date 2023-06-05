package com.example.onekids_project.entity.kids;


import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "evaluate_attach_file")
public class EvaluateAttachFile extends BaseEntity<String> {

    @Column(nullable = false)
    private String name;

    @Column(length = 1000, nullable = false)
    private String url;

    @Column(length = 1000, nullable = false)
    private String urlLocal;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_evaluate_date", nullable = false)
    private EvaluateDate evaluateDate;

}
