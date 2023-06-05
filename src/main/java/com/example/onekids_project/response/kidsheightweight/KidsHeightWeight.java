package com.example.onekids_project.response.kidsheightweight;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class KidsHeightWeight {
    private Double weight;

    private LocalDate timeWeight;

    private String nameWeight;

    private Double height;

    private LocalDate timeHeight;

    private String nameHeight;

}
