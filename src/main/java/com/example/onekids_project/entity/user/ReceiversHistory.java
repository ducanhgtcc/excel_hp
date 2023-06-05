package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntityId;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ma_receivers_history")
public class ReceiversHistory extends BaseEntityId<String> {

    @Column(nullable = false, columnDefinition = "bit default false")
    private boolean user_unread;

    private LocalDateTime time_read;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_receiver", nullable = false)
    private MaUser maUser;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_send", nullable = false)
    private AppSendHistory appSendHistory;

    private Long idSchool;

    private Long idClass;

    private Long idKids;

    private boolean sendDel;

    private boolean isApproved;
}
