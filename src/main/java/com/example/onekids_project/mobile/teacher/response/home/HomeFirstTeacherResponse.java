package com.example.onekids_project.mobile.teacher.response.home;

import com.example.onekids_project.mobile.parent.response.schoolconfig.SchoolConfigParent;
import com.example.onekids_project.security.payload.JwtDataObject;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class HomeFirstTeacherResponse {

    private LocalDate nowDate = LocalDate.now();

    private String quality;

    private String width;

    private SchoolConfigTeacher schoolConfig;

    private List<JwtDataObject> weekList;

    private List<JwtDataObject> monthList;
}
