package com.example.onekids_project.request.schoolconfig;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SubjectUpdateRequest extends IdRequest {
    @NotBlank
    private String subjectName;

    private String note;
}
