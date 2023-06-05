package com.example.onekids_project.mobile.teacher.request.evaluate;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ListEvaluateDateCreateTeacherRequest {
    @NotNull
    private Boolean status;

    @NotNull
    @Valid
    private List<EvaluateDateCreateTeacherRequest> dataList;
}
