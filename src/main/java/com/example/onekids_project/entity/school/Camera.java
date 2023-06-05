package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ma_camera")
public class Camera extends BaseEntity<String> {

    @Column(nullable = false)
    private String camName;

    @Column(nullable = false)
    private String linkCam;

    private String camChanel;

    private String camStream;

    private String camChanelOneCam;

    private String camStreamOneCam;

    private String note;

    @Column(nullable = false, columnDefinition = "bit default true")
    private boolean camActive = AppConstant.APP_TRUE;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dvr", nullable = false)
    private DvrCamera dvrCamera;

    @JsonBackReference
    @ManyToMany(mappedBy = "cameraList", fetch = FetchType.LAZY)
    List<MaClass> maClassList;

    @Column(nullable = false)
    private Long idSchool;
}
