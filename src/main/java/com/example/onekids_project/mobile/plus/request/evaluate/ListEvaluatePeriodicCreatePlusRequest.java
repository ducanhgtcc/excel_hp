package com.example.onekids_project.mobile.plus.request.evaluate;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ListEvaluatePeriodicCreatePlusRequest {

    @NotNull
    private Boolean status;

    @NotEmpty
    @Valid
    private List<EvaluatePeriodicCreatePlusRequest> dataList;
}
