package com.example.onekids_project.request.schoolconfig;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SubjectCreateRequest {
    @NotBlank
    private String subjectName;

    private String note;
}
