package com.example.onekids_project.request.schoolconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClassConfigSearchRequest {
    private Long idGrade;

    private Long idClass;

    private String className;
}
