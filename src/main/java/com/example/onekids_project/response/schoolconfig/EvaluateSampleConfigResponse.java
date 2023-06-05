package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateSampleConfigResponse extends IdResponse {
    private String evaluateType;

    private String evaluateContent;

    private Long idSchool;
}
