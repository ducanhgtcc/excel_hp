package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.example.onekids_project.response.kidsheightweight.HeightSampleResponse;
import com.example.onekids_project.response.kidsheightweight.KidsHeightResponse;
import com.example.onekids_project.response.kidsheightweight.KidsWeightResponse;
import com.example.onekids_project.response.kidsheightweight.WeightSampleResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KidsHeightWeightDTO extends IdDTO {

    private String fullName;

    private List<KidsHeightResponse> kidsHeightList;

    private List<KidsWeightResponse> kidsWeightList;

    private List<HeightSampleResponse> heightSampleResponseList;

    private List<WeightSampleResponse> weightSampleResponseList;

}
