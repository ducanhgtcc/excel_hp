package com.example.onekids_project.mobile.teacher.response.menuclass;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MenuImageWeekTeacherResponse {
    private String weekName;
    private List<String> pictureList;
}
