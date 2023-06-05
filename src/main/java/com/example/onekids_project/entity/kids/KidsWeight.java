package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "kids_weight")
public class KidsWeight extends BaseEntity<String> {

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private LocalDate timeWeight;

    @Column(nullable = false)
    private String appType;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kids;
}
