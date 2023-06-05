package com.example.onekids_project.mobile.parent.response.kids;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KidsHeightWeightParentResponse {
    private Long idHeight;

    private Long idWeight;

    private String age;

    private LocalDate date;

    private Double height;

    private Double weight;

    private boolean delete;

}
