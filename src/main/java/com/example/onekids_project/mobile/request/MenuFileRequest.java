package com.example.onekids_project.mobile.request;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MenuFileRequest extends IdRequest {

    @NotNull
    private Integer pageNumber;
}
