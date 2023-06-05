package com.example.onekids_project.importexport.model;

import com.example.onekids_project.response.kidsheightweight.KidsHeightResponse;
import com.example.onekids_project.response.kidsheightweight.KidsWeightResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class HeightWeightModel {

    private  Long id;

    private  Long stt;

    private String kidName;

    private Double weight;

    private LocalDate timeWeight;

    private Double Height;

    private LocalDate timeHeight;

    private List<KidsWeightModel> kidsWeightModelList;

    private List<KidsHeightModel> kidsHeightModelList;


}
