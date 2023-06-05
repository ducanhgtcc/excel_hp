package com.example.onekids_project.mobile.plus.request.kidsQuality;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CreateKidsHeightWeightPlusRequest {
    @NotNull
    private Long  idKid;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Double height;

    private Double weight;


}
