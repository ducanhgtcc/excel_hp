package com.example.onekids_project.mobile.teacher.response.menuclass;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MenuDateTeacherResponse {
    private String nameMeal;
    private String time;
    private List<String> foodList;
}
