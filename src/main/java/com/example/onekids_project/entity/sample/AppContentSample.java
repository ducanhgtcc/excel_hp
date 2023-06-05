package com.example.onekids_project.entity.sample;

import com.example.onekids_project.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "app_content_sample")
public class AppContentSample extends BaseEntity<String> {

    @Column(nullable = false, length = 255)
    private String viewType;

    @Column(nullable = false, length = 255)
    private String situationType;

    @Column(nullable = false, length = 255)
    private String userType;

    @Column(nullable = false, length = 500)
    private String content;

}
