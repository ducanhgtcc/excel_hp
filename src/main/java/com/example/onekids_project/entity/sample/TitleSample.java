package com.example.onekids_project.entity.sample;

import com.example.onekids_project.entity.base.BaseEntityId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "app_content_sample")
public class TitleSample extends BaseEntityId<String> {

    @Column(nullable = false, length = 255)
    private String appType;

    @Column(nullable = false, length = 255)
    private String sendType;

    @Column(nullable = false, length = 255)
    private String title;

}
