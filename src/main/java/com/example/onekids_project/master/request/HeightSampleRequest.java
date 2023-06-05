package com.example.onekids_project.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeightSampleRequest {
    private String yearOld;

    private double min;

    private double medium;

    private double max;

    private String type;
}
