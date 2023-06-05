package com.example.onekids_project.mobile.teacher.response.evaluate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateDateHaveTeacherResponse {
    private List<DateStatusObject> evaluateMonthList;

    private List<DateStatusObject> evaluateReplyMonthList;

}
