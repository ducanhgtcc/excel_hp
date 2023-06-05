package com.example.onekids_project.entity.system;

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
@Table(name = "web_system_title")
public class WebSystemTitle extends BaseEntity<String> {
    @Column(nullable = false)
    private String title = "title";

    private String content;

    private String source = "source";

    //lấy từ class SystemConstant: "web";"plus";"teacher";"system";
    @Column(nullable = false)
    private String type;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean sms = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean firebase = AppConstant.APP_TRUE;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean ott = AppConstant.APP_TRUE;

    private String note1 = "note1";

    private String note2 = "note2";
}
