package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ma_celebrate_sample")
public class CelebrateSample extends BaseEntity<String> {

    //in class SampleConstant
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    //Nam, Nữ, Tất cả
    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String month;

    @Column(length = 500)
    private String urlPicture;

    @Column(length = 500)
    private String urlPictureLocal;

    @Column(columnDefinition = "bit default 0")
    private boolean smsSend;

    @Column(columnDefinition = "bit default 0")
    private boolean appSend;

    @Column(columnDefinition = "bit default 0")
    private boolean active= AppConstant.APP_TRUE;

    //0 là của hệ thống, 1 là của các trường
    @Column(nullable = false)
    private Long idSchool;

}
