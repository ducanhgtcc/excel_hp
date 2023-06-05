package com.example.onekids_project.entity.system;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.School;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "sys_config")
public class SysConfig extends BaseEntity<String> {

    @Column(columnDefinition = "bit default false")
    private boolean appSendParent;

    @Column(columnDefinition = "bit default false")
    private boolean appSendTeacher;

    @Column(nullable = false)
    private int numberRemindParent;

    @Column(nullable = false)
    private int jumpTimeParent = 4;

    @Column(nullable = false)
    private int numberRemindTeacher;

    @Column(nullable = false, columnDefinition = "int default 2")
    private int jumpTimeTeacher = 2;

    @Column(nullable = false)
    private int numberRemindPlus;

    @Column(nullable = false, columnDefinition = "int default 2")
    private int jumpTimePlus = 2;

    @Column(nullable = false, columnDefinition = "time default '08:00:00'")
    private LocalTime timeFrom = LocalTime.of(8, 00, 00);

    @Column(nullable = false, columnDefinition = "time default '08:00:00'")
    private LocalTime timeTo = LocalTime.of(8, 00, 00);

    @Column(nullable = false, columnDefinition = "int default 10")
    private int numberOutSchool = 10;

    @Column(nullable = false, columnDefinition = "int default 3")
    private int numberNotify = 3;

    @Column(nullable = false, columnDefinition = "int default 3")
    private int jumpNumberOut = 3;

    @Column(nullable = false)
    private String qualityPicture = "60-80";

    @Column(nullable = false)
    private String widthPicture = "1280-1920";

    @Column(nullable = false, columnDefinition = "time default '08:00:00'")
    private LocalTime timeSendCelebrate = LocalTime.of(8, 0, 0);

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean showTitleSms = AppConstant.APP_TRUE;

    private String titleContentSms;

    private String phone1;

    private String phone2;

    private String phone3;

    private int widthAlbum;

    private int widthOther;

    private int mobileSizePage;

    private boolean appplusSmsSend;

    private boolean appteacherSmsSend;

    private boolean appparentSmsSend;

    private boolean userReceiveSms;

    private LocalDate dateActive;

    private LocalDate dateUnactive;

    //có xóa tự động tài khoản khi không còn đối tượng nào ko(phụ huynh->kids, nhân sự->infoEmployeeSchool)
    @Column(nullable = false, columnDefinition = "bit default true")
    private boolean deleteAccountStatus = true;

    //thời gian xóa là bao nhiều ngày
    @Column(nullable = false, columnDefinition = "int default 60")
    private int deleteAccountDate = 60;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false, unique = true)
    private School school;
}
