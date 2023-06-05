package com.example.onekids_project.mobile.plus.response.kidsQuality;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualityKidTeacherResponse extends IdResponse {
    private String nameKid;
    private String url;
    private Double weight;
    private Double height;
    private String date;
}
