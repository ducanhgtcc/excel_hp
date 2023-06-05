package com.example.onekids_project.response.employee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectInClassResponse extends IdResponse {
    private Long idClass;
    private String subjectName;
    private Boolean checkSubjectInClass;
}
