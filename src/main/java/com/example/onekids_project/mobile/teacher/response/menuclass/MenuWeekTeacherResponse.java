package com.example.onekids_project.mobile.teacher.response.menuclass;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MenuWeekTeacherResponse {
    private String date;
    private List<MenuDateTeacherResponse> menuDateTeacherResponseList;
}
