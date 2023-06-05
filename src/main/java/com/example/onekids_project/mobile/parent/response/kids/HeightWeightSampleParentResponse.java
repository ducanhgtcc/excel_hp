package com.example.onekids_project.mobile.parent.response.kids;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class HeightWeightSampleParentResponse {
    private String yearOld;

    @Column(nullable = false)
    private Double min;

    @Column(nullable = false)
    private Double medium;

    @Column(nullable = false)
    private Double max;
}
