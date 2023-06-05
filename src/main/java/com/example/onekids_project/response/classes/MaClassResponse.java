package com.example.onekids_project.response.classes;

import com.example.onekids_project.dto.GradeDTO;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MaClassResponse extends IdResponse {
    private String className;

    private String classDescription;

    private boolean morningSaturday;

    private boolean afternoonSaturday;

    private boolean eveningSaturday;

    private boolean sunday;

    private GradeDTO grade;
}
