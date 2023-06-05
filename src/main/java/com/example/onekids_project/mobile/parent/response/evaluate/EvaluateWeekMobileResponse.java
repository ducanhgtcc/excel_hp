package com.example.onekids_project.mobile.parent.response.evaluate;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EvaluateWeekMobileResponse extends LastPageBase {
    List<ListEvaluateWeekMobileResponse> dataList;

    public EvaluateWeekMobileResponse() {
        this.dataList = new ArrayList<>();
    }
}
