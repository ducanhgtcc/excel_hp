package com.example.onekids_project.mobile.teacher.response.evaluate;

import com.example.onekids_project.mobile.response.LastPageBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListEvaluateKidTeacherResponse extends LastPageBase {
    private List<EvaluateKidTeacherResponse> dataList;
}
