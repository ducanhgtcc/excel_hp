package com.example.onekids_project.entity.classes;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ma_class_menu")
public class ClassMenu extends BaseEntity<String> {

    @Column(nullable = false)
    private Long idSchool;

    @Column(nullable = false)
    private LocalDate menuDate;

    private String menuTitle;

    private String breakfastTime;

    @Column(length = 1000)
    private String breakfastContentList;

    private String secondBreakfastTime;

    @Column(length = 1000)
    private String secondBreakfastContentList;

    private String lunchTime;

    @Column(length = 1000)
    private String lunchContentList;

    private String afternoonTime;

    @Column(length = 1000)
    private String afternoonContentList;

    private String secondAfternoonTime;

    @Column(length = 1000)
    private String secondAfternoonContentList;

    private String dinnerTime;

    @Column(length = 1000)
    private String dinnerContentList;

    @Column(nullable = false)
    private boolean isApproved;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_class", nullable = false)
    private MaClass maClass;
}
