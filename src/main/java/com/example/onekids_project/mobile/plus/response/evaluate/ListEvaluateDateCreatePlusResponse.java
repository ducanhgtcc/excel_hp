package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.mobile.teacher.response.evaluate.EvaluateDateCreateTeacherResponse;
import lombok.Data;

import java.util.List;

@Data
public class ListEvaluateDateCreatePlusResponse {

    private int failNumber;

    private String message;

    private List<EvaluateDateCreatePlusResponse> dataList;
}
