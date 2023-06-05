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
@Table(name = "ma_birthday_sample")
public class BirthdaySample extends BaseEntity<String> {

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(length = 500)
    private String urlPicture;

    @Column(length = 500)
    private String urlPictureLocal;

    //học sinh, phụ huynh, nhân viên: nằm trong class SampleContant
    @Column(nullable = false)
    private String birthdayType;

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
