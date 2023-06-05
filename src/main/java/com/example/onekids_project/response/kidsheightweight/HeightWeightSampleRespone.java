package com.example.onekids_project.response.kidsheightweight;

import com.example.onekids_project.response.base.IdResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class HeightWeightSampleRespone {

    private String yearOld;

    private Double maxH;

    private Double mediumH;

    private Double minH;

    private Double maxW;

    private Double mediumW;

    private Double minW;
}
