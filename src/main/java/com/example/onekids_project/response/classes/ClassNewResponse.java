package com.example.onekids_project.response.classes;

import com.example.onekids_project.response.base.IdResponse;
import com.example.onekids_project.response.grade.GradeOtherResponse;
import com.example.onekids_project.response.grade.GradeUniqueResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClassNewResponse extends IdResponse {
    private String className;

    private String classDescription;

    private GradeUniqueResponse grade;

    //tên giáo viên chủ nhiệm
    private List<String> masterNameList;

    private int teacherNumber;

    private int employeeNumber;

    //số học sinh đang học
    private int studentStudyNumber;

    private int studentTotalNumber;
}
