package com.example.onekids_project.response.classes;

import com.example.onekids_project.response.employee.InfoEmployeeSchoolOtherResponse;
import com.example.onekids_project.response.subject.SubjectOtherResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TeacherInClassResponse {
    private InfoEmployeeSchoolOtherResponse infoEmployeeSchool;

    private boolean isMaster;

    private Set<SubjectOtherResponse> subjectSet;
}
