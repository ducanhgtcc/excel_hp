package com.example.onekids_project.request.base;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PageNumberWebRequest {

    @NotNull
    private Integer pageNumber;

    @NotNull
    private Integer maxPageItem;
}
