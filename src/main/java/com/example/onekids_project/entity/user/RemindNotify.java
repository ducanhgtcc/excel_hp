package com.example.onekids_project.entity.user;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name="ma_remind_notify")
public class RemindNotify extends BaseEntity<String> {

    private  Integer numberRemind;

    @Column(nullable = false)
    private int count_reminded;

    private LocalTime firsttimeRemind;

    @Column(nullable = false)
    private Boolean turnoffRemind= AppConstant.APP_TRUE;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private MaUser maUser;
}
