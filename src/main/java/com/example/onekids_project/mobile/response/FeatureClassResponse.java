package com.example.onekids_project.mobile.response;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeatureClassResponse extends IdResponse {

    private List<TeacherClassResponse> teacherClassList;

    private String className;

    private int numberKid;


}
