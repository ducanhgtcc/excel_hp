package com.example.onekids_project.mobile.parent.response.schoolconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolConfigParent {
    private EatConfig eatConfig;

    private LearnConfig learnConfig;

    private EvaluateConfig evaluateConfig;

    private AbsentConfig absentConfig;

    private ShowAttendanceConfig showAttendanceConfig;
}
