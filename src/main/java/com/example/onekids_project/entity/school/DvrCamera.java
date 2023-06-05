package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_dvr_camera")
public class DvrCamera extends BaseEntity<String> {

    @Column(nullable = false)
    private String dvrName;

    @Column(nullable = false)
    private String dvrType;

    @Column(nullable = false)
    private String adminDvrAcc;

    @Column(nullable = false)
    private String adminDvrPassword;

    @Column(nullable = false)
    private String schoolDomain;

    @Column(nullable = false)
    private String camPort;

    @Column(nullable = false)
    private String linkDvr;

    private String ipLocal;

    private String modemAcc;

    private String modemPass;

    private String note;

    private String deviceSN;

    private String portDVR;

    @Column(nullable = false, columnDefinition = "bit default true")
    private boolean dvrActive = AppConstant.APP_TRUE;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false)
    private School school;

    @JsonManagedReference
    @OneToMany(mappedBy = "dvrCamera", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Camera> cameraList;
}
