package com.example.onekids_project.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeightSampleRequest {
    private String yearOld;

    private Double min;

    private Double medium;

    private Double max;

    private String type;
}
