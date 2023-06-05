package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateStatusKidDayPlusResponse extends IdResponse {

    private String nameClass;

    private int totalKid;

    private int absent;

    private int goSchool;

    private int evaluateYes;

    private int evaluateNo;

    private boolean evaluate;

}
