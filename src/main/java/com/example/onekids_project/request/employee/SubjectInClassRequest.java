package com.example.onekids_project.request.employee;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectInClassRequest extends IdRequest {
    private Long idClass;
    private String subjectName;
}
