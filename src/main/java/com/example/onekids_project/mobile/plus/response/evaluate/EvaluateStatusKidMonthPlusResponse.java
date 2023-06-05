package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateStatusKidMonthPlusResponse extends IdResponse {

    private String nameClass;

    // ngày có nhận xét
    private int evaluateDayYes;

    // ngày ko có nhận xét
    private int evaluateDayNo;

    // số học sinh không có nhận xét
    private int kidEvaluateNo;

    private int kidEvaluateWeekYes;

    private int kidEvaluateMonthYes;

    private int kidEvaluatePeriodicYes;

}
