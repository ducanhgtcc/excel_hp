package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "app_icon_plus")
public class AppIconPlus extends BaseEntity<String> {

    @Column(columnDefinition = "bit default 1")
    private boolean employee = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean kids = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean feedback = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean attendance = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean message = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean medicine = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean absent = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean evaluate = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean album = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean video = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean camera = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean smsAppHistory = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean learn = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean menu = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean birthday = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean notify = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean salary = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean utility = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean facebook = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean support = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean studentFees = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean health = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean cashInternal = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean wallet = AppConstant.APP_TRUE;

    @Column(columnDefinition = "bit default 1")
    private boolean notifySchool = AppConstant.APP_TRUE;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false, unique = true)
    private School school;

    @Column(nullable = false)
    private String employeeName = AppIconName.EMPLOYEE;

    @Column(nullable = false)
    private String kidsName = AppIconName.KIDS;

    @Column(nullable = false)
    private String messageName = AppIconName.MESSAGE;

    @Column(nullable = false)
    private String medicineName = AppIconName.MEDICINE;

    @Column(nullable = false)
    private String absentName = AppIconName.ABSENT;

    @Column(nullable = false)
    private String attendanceName = AppIconName.ATTENDANCE;

    @Column(nullable = false)
    private String evaluateName = AppIconName.EVALUATE;

    @Column(nullable = false)
    private String albumName = AppIconName.ALBUM;

    @Column(nullable = false)
    private String healthName = AppIconName.HEALTH;

    @Column(nullable = false)
    private String learnName = AppIconName.LEARN;

    @Column(nullable = false)
    private String menuName = AppIconName.MENU;

    @Column(nullable = false)
    private String videoName = AppIconName.VIDEO;

    @Column(nullable = false)
    private String cameraName = AppIconName.CAMERA;

    @Column(nullable = false)
    private String birthdayName = AppIconName.BIRTHDAY;

    @Column(nullable = false)
    private String studentFeesName = AppIconName.STUDENT_FEES;

    @Column(nullable = false)
    private String salaryName = AppIconName.SALARY;

    @Column(nullable = false)
    private String utilityName = AppIconName.UTILITY;

    @Column(nullable = false)
    private String facebookName = AppIconName.FACEBOOK;

    @Column(nullable = false)
    private String feedbackName = AppIconName.FEEDBACK;

    @Column(nullable = false)
    private String smsAppHistoryName = AppIconName.SMS_APP_HISTORY_NAME;

    @Column(nullable = false)
    private String notifyName = AppIconName.NOTIFY_NAME;

    @Column(nullable = false)
    private String supportName = AppIconName.SUPPORT_NAME;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'Thu chi nội bộ'")
    private String cashInternalName = AppIconName.CASH_INTERNAL_NAME;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'Ví'")
    private String walletName = AppIconName.WALLET_NAME;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'Quản lý thông báo'")
    private String notifySchoolName = AppIconName.NOTIFY_SCHOOL_NAME;

    @Column(nullable = false)
    private String newsName = AppIconName.NEWS_NAME;

    //click khi khoa
    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String employeeLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String kidsLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String feedbackLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String attendanceLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String messageLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String medicineLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String absentLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String evaluateLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String albumLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String videoLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String cameraLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String smsAppHistoryLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String learnLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String menuLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String birthdayLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String notifyLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String salaryLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String utilityLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String facebookLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String supportLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String studentFeesLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String healthLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String cashInternalLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String walletLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String notifySchoolLock;

    @Column(columnDefinition = "varchar(255) default 'Bạn không thể sử dụng chức năng này'")
    private String newsLock;

}
