package com.example.onekids_project.request.base;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class PageWebRequest {
    @NotNull
    private Integer pageNumber;

    @NotNull
    private Integer maxPageItem;

    public void setPageNumber(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("Page index must not be less than 1");
        }
        this.pageNumber = page - 1;
    }

    public void setMaxPageItem(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than 1");
        }
        this.maxPageItem = size;
    }
}
