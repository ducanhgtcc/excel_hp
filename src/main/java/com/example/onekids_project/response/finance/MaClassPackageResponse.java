package com.example.onekids_project.response.finance;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.grade.GradeUniqueResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaClassPackageResponse extends IdResponse {
    private String className;

    private GradeUniqueResponse grade;

    private int number;
}
