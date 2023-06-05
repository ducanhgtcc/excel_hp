package com.example.onekids_project.mobile.parent.response.kids;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListHeightWeightSampleParentResponse {
    private List<HeightWeightSampleParentResponse> heightSampleList;

    private List<HeightWeightSampleParentResponse> weightSampleList;
}
