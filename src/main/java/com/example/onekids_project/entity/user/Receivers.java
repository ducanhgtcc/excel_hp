package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ma_receivers")
public class Receivers extends BaseEntity<String> {

    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean userUnread;

    private LocalDateTime timeRead;

    private boolean isApproved;

    @Column(columnDefinition = "bit default 0")
    private boolean sendDel;

    @Column(nullable = false)
    private Long idUserReceiver;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_send", nullable = false)
    private AppSend appSend;

    @Column(nullable = false)
    private Long idSchool;

    private Long idClass;

    private Long idKids;



}
