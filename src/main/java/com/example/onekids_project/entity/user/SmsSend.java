package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_sms_send")
public class SmsSend extends BaseEntity<String> {

    private String titleContent;

    @Column(nullable = false ,length = 5000)
    private String sendContent;

    // sinh nhat, ngay le, chung,sys (AppSendConstant)
    @Column(nullable = false)
    private String sendType;

    private LocalDateTime timeAlarm;

    // apptype cua nguoi gui - lay trong ApptypeContant
    @Column(nullable = false)
    private String appType;
    // số người nhận
    private int smsNumber;
    // số gói tin
    private int smsTotal;

    @Column(nullable = false)
    private String serviceProvider;

    private Long id_user_send;

    // true là đã gửi - false là chờ gửi
    private boolean sent;

    // hệ thống gửi idschool = 0, trường gửi id != 0
    @Column(nullable = false)
    private Long id_school;

    private Long idKid;

    @JsonManagedReference
    @OneToMany(mappedBy = "smsSend", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<SmsReceivers> smsReceiversList;
}
