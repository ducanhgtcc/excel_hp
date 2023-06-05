package com.example.onekids_project.request.evaluatekids;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluatePeriodicSearchRequest {
    private Long idGrade;

    private Long idClass;

    private Boolean approved;
}
