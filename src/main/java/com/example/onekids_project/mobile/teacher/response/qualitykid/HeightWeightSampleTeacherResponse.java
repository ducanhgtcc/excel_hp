package com.example.onekids_project.mobile.teacher.response.qualitykid;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
@Getter
@Setter
public class HeightWeightSampleTeacherResponse {
    private String yearOld;

    @Column(nullable = false)
    private Double min;

    @Column(nullable = false)
    private Double medium;

    @Column(nullable = false)
    private Double max;
}
