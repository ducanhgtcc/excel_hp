package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.common.EvaluateConstant;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.validate.anotationcustom.StringInList;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EvaluateSampleUpdateRequest extends IdRequest {
    @NotBlank
    @StringInList(values = {EvaluateConstant.LEARN, EvaluateConstant.EAT, EvaluateConstant.SLEEP, EvaluateConstant.SANITARY, EvaluateConstant.HEALT, EvaluateConstant.COMMON})
    private String evaluateType;

    @NotBlank
    private String evaluateContent;
}
