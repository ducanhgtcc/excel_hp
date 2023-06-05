package com.example.onekids_project.request.evaluatekids;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EvaluateDateSearchRequest {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

//    private Long idGrade;

    @NotNull
    private Long idClass;

    private Boolean approved;
}
