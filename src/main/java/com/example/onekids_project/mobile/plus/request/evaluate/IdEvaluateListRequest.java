package com.example.onekids_project.mobile.plus.request.evaluate;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class IdEvaluateListRequest {
    @NotEmpty
    private List<Long> idList;
}
