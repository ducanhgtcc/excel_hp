package com.example.onekids_project.response.grade;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GradeResponse extends IdResponse {
    private String gradeName;

    private String gradeDescription;
}
