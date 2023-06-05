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
@Table(name = "height_sample")
public class HeightSample extends BaseEntity<String> {

    @Column(nullable = false, length = 255)
    private String yearOld;

    @Column(nullable = false)
    private Double min;

    @Column(nullable = false)
    private Double medium;

    @Column(nullable = false)
    private Double max;

    @Column(nullable = false, length = 255)
    private String type;

}
