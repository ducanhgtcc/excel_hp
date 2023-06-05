package com.example.onekids_project.response.grade;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeUniqueResponse extends IdResponse {
    private String gradeName;
}
