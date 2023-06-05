package com.example.onekids_project.response.kidsheightweight;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class HeightSampleResponse extends IdResponse {

    private Double max;

    private Double medium;

    private Double min;

    private String type;

    private String yearOld;
}
