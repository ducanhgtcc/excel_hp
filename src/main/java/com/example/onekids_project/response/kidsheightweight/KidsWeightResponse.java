package com.example.onekids_project.response.kidsheightweight;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class KidsWeightResponse extends IdResponse {
    private Double weight;

    private LocalDate timeWeight;

    private String appType;

    private boolean delActive;

    private String createdBy;

}
