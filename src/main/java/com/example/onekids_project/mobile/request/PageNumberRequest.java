package com.example.onekids_project.mobile.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PageNumberRequest {
    @NotNull
    private Integer pageNumber;
}
