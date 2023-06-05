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
@Table(name = "weight_sample")
public class WeightSample extends BaseEntity<String> {

    @Column(nullable = false)
    private String yearOld;

    @Column(nullable = false)
    private double min;

    @Column(nullable = false)
    private double medium;

    @Column(nullable = false)
    private double max;

    // boy or girl
    @Column(nullable = false)
    private String type;

}
