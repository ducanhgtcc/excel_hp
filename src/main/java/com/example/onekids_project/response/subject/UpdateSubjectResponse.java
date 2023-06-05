package com.example.onekids_project.response.subject;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSubjectResponse extends IdResponse {

    private String subjectName;

    private String note;
}
