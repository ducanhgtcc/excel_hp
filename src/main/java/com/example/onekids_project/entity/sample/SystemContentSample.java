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
@Table(name = "system_content_sample")
public class SystemContentSample extends BaseEntityId<String> {

    @Column(nullable = false, length = 1000)
    private String content;

    private String type;
}
