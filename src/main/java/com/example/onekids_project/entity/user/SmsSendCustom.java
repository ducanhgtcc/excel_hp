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
@Table(name = "sms_send_custom")
public class SmsSendCustom extends BaseEntity<String> {

    @Column(length = 500)
    private String sendTitle;

//    @Column(nullable = false)
//    private LocalDateTime timeSend;

    private String sendType;

    private Long idSchool;

    private String appType;

    //số người nhận
    private Integer receivedCount;

    @Column(nullable = false)
    private String serviceProvider;

    @JsonManagedReference
    @OneToMany(mappedBy = "smsSendCustom", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<SmsReceiversCustom> smsReceiversCustomList;
}
