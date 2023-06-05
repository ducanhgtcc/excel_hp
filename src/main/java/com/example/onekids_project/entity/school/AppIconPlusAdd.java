package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "app_icon_plus_add")
public class AppIconPlusAdd extends BaseEntity<String> {

    private boolean employee;

    private boolean kids;

    private boolean feedback;

    private boolean attendance;

    private boolean message;

    private boolean medicine;

    private boolean absent;

    private boolean evaluate;

    private boolean album;

    private boolean video;

    private boolean camera;

    private boolean smsAppHistory;

    private boolean learn;

    private boolean menu;

    private boolean birthday;

    private boolean notify;

    private boolean salary;

    private boolean utility;

    private boolean facebook;

    private boolean support;

    private boolean studentFees;

    private boolean health;

    @Column(columnDefinition = "bit default 1")
    private boolean cashInternal;

    @Column(columnDefinition = "bit default 1")
    private boolean wallet;

    @Column(columnDefinition = "bit default 1")
    private boolean notifySchool;

    @Column(columnDefinition = "bit default 1")
    private boolean news = AppConstant.APP_TRUE;

    //ẩn hiện
    @Column(columnDefinition = "bit default 1")
    private boolean employeeShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean kidsShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean feedbackShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean attendanceShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean messageShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean medicineShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean absentShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean evaluateShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean albumShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean videoShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean cameraShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean smsAppHistoryShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean learnShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean menuShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean birthdayShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean notifyShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean salaryShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean utilityShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean facebookShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean supportShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean studentFeesShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean healthShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean cashInternalShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean walletShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean notifySchoolShow = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean newsShow = AppConstant.APP_TRUE;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "id_info_employee", nullable = false, unique = true)
    private InfoEmployeeSchool infoEmployeeSchool;

    @Column(nullable = false)
    private Long idSchool;

}
