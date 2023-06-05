package com.example.onekids_project.mobile.plus.response.home;

import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.response.classes.MaClassOtherResponse;
import com.example.onekids_project.response.grade.GradeOtherResponse;
import com.example.onekids_project.response.grade.GradeUniqueResponse;
import com.example.onekids_project.security.payload.JwtDataObject;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class HomeFirstPlusResponse {
    private LocalDate nowDate = LocalDate.now();

    private String quality;

    private String width;

    private List<MaClassOtherResponse> classList;

    private List<GradeUniqueResponse> gradeList;

    private List<JwtDataObject> weekList;

    private List<JwtDataObject> monthList;

    private SchoolConfigPlus schoolConfig;

}
