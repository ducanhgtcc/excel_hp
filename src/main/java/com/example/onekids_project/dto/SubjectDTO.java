package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.BaseDTO;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.ExKidsSubject;
import com.example.onekids_project.entity.kids.EvaluateSubject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectDTO extends BaseDTO<String> {

    private String subjectCode;

    private String subjectName;

    private String note;

    private Long id_school;

    private ExEmployeeClass exEmployeeClass;

    private List<ExKidsSubject> exKidsSubjectList;

    private List<EvaluateSubject> evaluateSubjectList;

    int pageNumber;

    int maxPageItem;
}
