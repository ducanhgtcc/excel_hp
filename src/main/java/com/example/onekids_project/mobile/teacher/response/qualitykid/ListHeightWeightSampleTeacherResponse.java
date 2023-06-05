package com.example.onekids_project.mobile.teacher.response.qualitykid;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListHeightWeightSampleTeacherResponse {

    private List<HeightWeightSampleTeacherResponse> heightSampleList;

    private List<HeightWeightSampleTeacherResponse> weightSampleList;
}
