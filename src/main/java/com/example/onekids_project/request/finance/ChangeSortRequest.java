package com.example.onekids_project.request.finance;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeSortRequest {
    @NotNull
    private Long id1;

    @NotNull
    private Long id2;
}
