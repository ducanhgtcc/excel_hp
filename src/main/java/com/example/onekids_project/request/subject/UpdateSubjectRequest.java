package com.example.onekids_project.request.subject;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateSubjectRequest extends IdRequest {

    private String subjectName;

    private String note;
}
