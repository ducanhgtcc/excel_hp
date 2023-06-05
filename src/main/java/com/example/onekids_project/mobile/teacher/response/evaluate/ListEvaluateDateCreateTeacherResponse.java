package com.example.onekids_project.mobile.teacher.response.evaluate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEvaluateDateCreateTeacherResponse {

    private int failNumber;

    private String message;

    private List<EvaluateDateCreateTeacherResponse> dataList;
}
