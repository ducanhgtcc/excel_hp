package com.example.onekids_project.mobile.parent.response.evaluate;

import com.example.onekids_project.common.AppConstant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateOfMonthMobileResponse {
    private int date;

    private boolean status = AppConstant.APP_TRUE;
}
