package com.example.onekids_project.mobile.parent.request.kids;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class HeightWeightForTable {
    private LocalDate checkDay;

    private Long idHeight;

    private Integer ageHeight;

    private Double height;

    private String heightAppType;

    private Long idWeight;

    private Integer ageWeight;

    private Double weight;

    private String weightAppType;

}
