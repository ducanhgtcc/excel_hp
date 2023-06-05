package com.example.onekids_project.mobile.plus.response.kidsQuality;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KidsQualityPlusResponse extends IdResponse {

    private String fullName;

    private String url;

    private double weight;

    private double height;

    private String date;

}
