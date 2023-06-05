package com.example.onekids_project.entity.onecam;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.user.MaUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
public class DeviceCam extends BaseEntity<String> {

    @Column(nullable = false, length = 1000)
    private String idDevice;

    //android, ios
    private String type;

    //true là đã login, false là chưa login
    private boolean login;

    //true->bi ep dang xuat
    private boolean forceLogout;

    private LocalDateTime dateLogin;

    private LocalDateTime dateLogout;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private MaUser maUser;

}
