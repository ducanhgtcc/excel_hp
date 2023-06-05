package com.example.onekids_project.mobile.plus.response.home;

import com.example.onekids_project.mobile.parent.response.schoolconfig.AbsentConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EatConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EvaluateConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.LearnConfig;
import com.example.onekids_project.mobile.teacher.response.home.AttendanceKidsConfig;
import com.example.onekids_project.mobile.teacher.response.home.CommonConfigTeacher;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolConfigPlus {
    private EatConfig eatConfig;

    private LearnConfig learnConfig;

    private EvaluateConfig evaluateConfig;

    private AttendanceKidsConfig attendanceKidsConfig;

    private ShowConfigCommonPlus showConfigCommonPlus;
}
