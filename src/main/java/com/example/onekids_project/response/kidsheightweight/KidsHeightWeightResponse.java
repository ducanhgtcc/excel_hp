package com.example.onekids_project.response.kidsheightweight;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.kids.KidMainExportRespone;
import com.example.onekids_project.response.kids.KidOtherResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class KidsHeightWeightResponse extends IdResponse {

    private String fullName;

    private Double weight;

    private LocalDate timeWeight;

    private Double height;

    private LocalDate timeHeight;

    private LocalDate birthDay;

    private KidOtherResponse kids;

    @JsonBackReference
    private List<KidsHeightResponse> kidsHeightList;

    @JsonBackReference
    private List<KidsWeightResponse> kidsWeightList;


}
