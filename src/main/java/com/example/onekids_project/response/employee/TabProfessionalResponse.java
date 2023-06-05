package com.example.onekids_project.response.employee;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TabProfessionalResponse extends IdResponse {
    private String className;
    private Boolean isMaster;
    private Boolean checkIsClass;
//    List<Long> idSubjectInClassList;
    List<Long> listIdSubject;
    List<SubjectInClassResponse> subjectResponseList;
}
