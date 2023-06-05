package com.example.onekids_project.entity.system;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ma_news_extra")
public class NewsExtra extends BaseEntity<String> {

    @Column(nullable = false, length = 1000)
    private String title;

    @Column(nullable = false, length = 1000)
    private String link;

    private boolean appPlus;

    private boolean appPlusUsed;

    private boolean appTeacher;

    private boolean appTeacherUsed;

    private boolean appParent;

    private boolean appParentUsed;

}
