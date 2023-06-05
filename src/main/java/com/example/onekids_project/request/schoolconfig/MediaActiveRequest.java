package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MediaActiveRequest extends IdRequest {
    @NotNull
    private Boolean mediaActive;
}
