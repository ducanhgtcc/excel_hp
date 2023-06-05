package com.example.onekids_project.mobile.teacher.response.home;

import com.example.onekids_project.mobile.parent.response.schoolconfig.AbsentConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EatConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.EvaluateConfig;
import com.example.onekids_project.mobile.parent.response.schoolconfig.LearnConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolConfigTeacher {
    private EatConfig eatConfig;

    private LearnConfig learnConfig;

    private EvaluateConfig evaluateConfig;

    private AbsentConfig absentConfig;

    private AttendanceKidsConfig attendanceKidsConfig;

    private CommonConfigTeacher commonConfig;

    private ShowConfigCommonTeacher showConfigCommonTeacher;
}
