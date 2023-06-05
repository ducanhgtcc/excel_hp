package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.mobile.response.LastPageBase;
import com.example.onekids_project.mobile.teacher.response.evaluate.EvaluateKidTeacherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEvaluateKidPlusResponse extends LastPageBase {
    private List<EvaluateKidPlusResponse> dataList;
}
