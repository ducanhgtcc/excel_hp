package com.example.onekids_project.mobile.teacher.response.qualitykid;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KidsHeightWeightTeacherResponse {
    private Long idHeight;

    private Long idWeight;

//    private String height;
//
//    private String weight;

    private String age;

    private String date;

    private Double height;

    private Double weight;

    private boolean delete;
}
