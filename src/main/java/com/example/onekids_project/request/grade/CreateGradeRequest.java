package com.example.onekids_project.request.grade;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateGradeRequest {
    @NotBlank
    private String gradeName;

    private String gradeDescription;
}
