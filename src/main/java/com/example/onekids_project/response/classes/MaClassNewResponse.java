package com.example.onekids_project.response.classes;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.grade.GradeUniqueResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaClassNewResponse extends IdResponse {
    private String className;

    private String classDescription;

    private GradeUniqueResponse grade;
}
