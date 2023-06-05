package com.example.onekids_project.entity.kids;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.school.School;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "kids_extra_info")
public class KidsExtraInfo extends BaseEntity<String> {

    private String bloodType;

    private String swim;

    private String allery;

    private String diet;

    private String ear;

    private String nose;

    private String throat;

    private String eyes;

    private String skin;

    private String heart;

    private String fat;

    private String note;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kid", nullable = false, unique = true)
    private Kids kid;

    @Column(nullable = false)
    private Long idSchool;
}
