package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Kids;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "app_icon_parent_add")
public class AppIconParentAdd extends BaseEntity<String> {

    private boolean message;

    private boolean medicine;

    private boolean absent;

    private boolean album;

    private boolean evaluate;

    private boolean attendance;

    private boolean studentFees;

    private boolean learn;

    private boolean menu;

    private boolean video;

    private boolean camera;

    private boolean kidsInfo;

    private boolean utility;

    private boolean facebook;

    private boolean feedback;

    @Column(columnDefinition = "bit default 1")
    private boolean news = AppConstant.APP_TRUE;

    //ẩn hiện
    @Column(columnDefinition = "bit default 1")
    private boolean messageShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean medicineShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean absentShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean albumShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean evaluateShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean attendanceShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean studentFeesShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean learnShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean menuShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean videoShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean cameraShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean kidsInfoShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean utilityShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean facebookShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean feedbackShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean newsShow = AppConstant.APP_TRUE;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false, unique = true)
    private Kids kid;

    @Column(nullable = false)
    private Long idSchool;
}
