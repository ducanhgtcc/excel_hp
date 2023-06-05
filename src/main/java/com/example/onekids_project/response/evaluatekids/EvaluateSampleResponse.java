package com.example.onekids_project.response.evaluatekids;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateSampleResponse extends IdResponse {
    private String evaluateType;

    private String evaluateContent;
}
