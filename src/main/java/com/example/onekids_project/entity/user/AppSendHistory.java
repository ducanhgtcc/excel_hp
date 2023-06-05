package com.example.onekids_project.entity.user;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_app_send_history")
public class AppSendHistory extends BaseEntity<String> {

    @Column(nullable = false)
    private String sendTitle;

    @Column(nullable = false)
    private String sendContent;

    @Column(nullable = false)
    private Integer received_number;

    private String attachFile;

    private Integer countAttachFile;

    private String attachPicture;

    private Integer countAttachPicture;

    private LocalDateTime timeSend;

    private boolean sendDel= AppConstant.APP_FALSE;

    private boolean isApproved;

    private Long id_school;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_send", nullable = false)
    private MaUser maUser;

    @JsonManagedReference
    @OneToMany(mappedBy = "appSendHistory", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ReceiversHistory> receiversHistoryList;

}
