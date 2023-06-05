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
@Table(name = "ma_app_send")
public class AppSend extends BaseEntity<String> {

    //AppSendConstant
    @Column(nullable = false)
    private String sendType;

    @Column(nullable = false)
    private String sendTitle;

    @Column(nullable = false,  columnDefinition = "TEXT")
    private String sendContent;

    @Column(nullable = false)
    private int receivedNumber;

    private LocalDateTime timeSend;

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean sendDel;

    private boolean isApproved;

    @Column(nullable = false)
    private Long idSchool;

    //AppTypeConstant
    //appType người gửi
    @Column(nullable = false)
    private String appType;

    // Người nhận: parent, teacher
    private String typeReicever;

    @JsonManagedReference
    @OneToMany(mappedBy = "appSend", cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Receivers> receiversList;

    @JsonManagedReference
    @OneToMany(mappedBy = "appSend", cascade = {CascadeType.PERSIST, CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<UrlFileAppSend> urlFileAppSendList;

}
