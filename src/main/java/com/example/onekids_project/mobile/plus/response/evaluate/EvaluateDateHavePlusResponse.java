package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.mobile.teacher.response.evaluate.DateStatusObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateDateHavePlusResponse {

    private List<DateStatusObject> evaluateMonthList;

    private List<DateStatusObject> evaluateReplyMonthList;
}
