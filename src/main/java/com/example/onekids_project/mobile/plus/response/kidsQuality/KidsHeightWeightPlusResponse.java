package com.example.onekids_project.mobile.plus.response.kidsQuality;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsHeightWeightPlusResponse {
    private Long idHeight;

    private Long idWeight;

    private String age;

    private String date;

    private Double height;

    private Double weight;

    private boolean delete;
}
