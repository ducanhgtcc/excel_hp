package com.example.onekids_project.entity.user;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_device")
public class Device extends BaseEntity<String> {

    @Column(nullable = false, length = 1000)
    private String idDevice;

    //DeviceTypeConstant: web, android, ios
    @Column(nullable = false)
    private String type;

    //true là đã login, false là chưa login
    private boolean login;

    private LocalDateTime dateLogin;

    private LocalDateTime dateLogout;

    @Column(length = 1000)
    private String tokenFirebase;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private MaUser maUser;

}
