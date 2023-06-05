package com.example.onekids_project.response.schoolconfig;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectForClassResponse extends IdResponse {
    private String subjectName;

    private String note;

    private boolean used;
}
