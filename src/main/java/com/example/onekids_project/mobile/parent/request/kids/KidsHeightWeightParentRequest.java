package com.example.onekids_project.mobile.parent.request.kids;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class KidsHeightWeightParentRequest {
    @NotNull
    private LocalDate date;

    private Double height;

    private Double weight;
}
