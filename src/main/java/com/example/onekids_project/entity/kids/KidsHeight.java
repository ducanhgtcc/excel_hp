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
@Table(name = "kids_height")
public class KidsHeight extends BaseEntity<String> {

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private LocalDate timeHeight;

    @Column(nullable = false)
    private String appType;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kids;
}
