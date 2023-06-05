package com.example.onekids_project.request.evaluatekids;

import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateSanitaryRequest extends BaseDTO<String>  {
    private String itemName;

    private String content;
}