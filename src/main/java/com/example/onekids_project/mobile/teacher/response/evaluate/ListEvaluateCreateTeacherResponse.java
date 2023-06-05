package com.example.onekids_project.mobile.teacher.response.evaluate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEvaluateCreateTeacherResponse {
    private int failNumber;

    private String message;

    private List<EvaluateCreateTeacherResponse> dataList;
}
