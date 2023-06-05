package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ma_sms_receivers")
public class SmsReceivers extends BaseEntity<String> {

    //    @Column(nullable = false)
    private Long idUserReceiver;

    @Column
    private Long idKid;

    @Column(nullable = false)
    private String phone;

    // hệ thống gửi idschool = 0, trường gửi id != 0
    @Column(nullable = false)
    private Long idSchool;

    @JsonBackReference
    @ManyToOne()
    @JoinColumn(name = "id_sms_code")
    private SmsCode smsCode;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sms_send", nullable = false)
    private SmsSend smsSend;
}
