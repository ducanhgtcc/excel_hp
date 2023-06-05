package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sms_code")
public class SmsCode extends BaseEntity<String> {
    private String codeError;

    private double serviceProvider;

    private String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "smsCode", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<SmsReceivers> smsReceiversList;

    @JsonManagedReference
    @OneToMany(mappedBy = "smsCode", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<SmsReceiversCustom> smsReceiversCustomList;
}
