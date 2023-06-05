package com.example.onekids_project.mobile.plus.response.evaluate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEvaluateCreatePlusResponse {

    private int failNumber;

    private String message;

    private List<EvaluateCreatePlusResponse> dataList;
}
