package com.example.onekids_project.request.classes;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateMaClassRequest {
    @NotBlank
    private String className;

    private String classDescription;

    @NotNull
    private Long idGrade;
}
