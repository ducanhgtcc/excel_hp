package com.example.onekids_project.dto;

import com.example.onekids_project.dto.base.IdDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MaClassDTO extends IdDTO {

    private String classCode;

    private String className;

    private String classDescription;

    private Long idSchool;

    private boolean morningSaturday;

    private boolean afternoonSaturday;

    private boolean eveningSaturday;

    private boolean sunday;

    private int kidsNumber;

    private int teacherNumber;

    private String masterTeacherList;

    private boolean delActive;

    @JsonBackReference
    private GradeDTO grade;

    private GradeDTO gradeResponse;

    @JsonBackReference
    private List<ExEmployeeClassDTO> exEmployeeClassList;

    private List<ExEmployeeClassDTO> exEmployeeClassListResponse;

//    @JsonBackReference
    private List<KidsDTO> kidsList;

    private List<KidsDTO> kidsListResponse;


}
