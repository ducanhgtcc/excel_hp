package com.example.onekids_project.mobile.teacher.request.qualitykid;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class KidsHeightWeightTeacherRequest {
    @NotNull
    private Long  idKid;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Double height;

    private Double weight;
}
