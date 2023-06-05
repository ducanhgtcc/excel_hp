package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.base.BaseEntityId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "sms_receivers_custom")
public class SmsReceiversCustom extends BaseEntity<String> {

    @Column(nullable = false)
    private String phoneUserReceiver;

    private String nameUserReceiver;

    @Column(nullable = false, length = 500)
    private String sendContent;

    private String appType;

    private Long idKid;

    private int numberSms;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_send_custom", nullable = false)
    private SmsSendCustom smsSendCustom;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sms_code", nullable = false)
    private SmsCode smsCode;

    @Column(nullable = false)
    private Long idSchool;

}
