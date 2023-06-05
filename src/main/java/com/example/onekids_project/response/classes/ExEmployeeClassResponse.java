package com.example.onekids_project.response.classes;

import com.example.onekids_project.dto.InfoEmployeeSchoolDTO;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExEmployeeClassResponse extends IdResponse {
    private InfoEmployeeSchoolDTO infoEmployeeSchool;

    private String teacherName;

    private boolean isMaster;

    private String listIdSubject;

    private String subjectNameList;
}
