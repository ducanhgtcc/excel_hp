package com.example.onekids_project.request.classes;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateMaClassRequest extends IdRequest {
    @NotBlank
    private String className;

    private String classDescription;
}
