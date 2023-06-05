package com.example.onekids_project.response.classes;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeClassNameResponse extends IdResponse {
    private String className;

    private String gradeName;
}
