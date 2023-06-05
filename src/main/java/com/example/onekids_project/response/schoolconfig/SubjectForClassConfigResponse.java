package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.subject.SubjectOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectForClassConfigResponse extends IdResponse {
    private String className;

    private List<SubjectOtherResponse> subjectList;
}
