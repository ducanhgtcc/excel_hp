package com.example.onekids_project.response.kidsheightweight;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeightSampleResponse extends IdResponse {

    private Double max;

    private Double medium;

    private Double min;

    private String yearOld;

    private String type;


}
