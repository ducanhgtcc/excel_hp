package com.example.onekids_project.mobile.parent.response.evaluate;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EvaluateMonthMobileResponse extends LastPageBase {
    List<ListEvaluateMonthMobileResponse> dataList;

    public EvaluateMonthMobileResponse() {
        this.dataList = new ArrayList<>();
    }
}
