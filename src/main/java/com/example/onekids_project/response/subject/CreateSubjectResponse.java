package com.example.onekids_project.response.subject;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubjectResponse extends IdResponse {

    private String subjectName;

    private String note;
}
