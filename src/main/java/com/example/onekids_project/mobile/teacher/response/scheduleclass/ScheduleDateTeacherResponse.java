package com.example.onekids_project.mobile.teacher.response.scheduleclass;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ScheduleDateTeacherResponse {
    private String title;

    private List<ScheduleTeacherResponse> morningList;

    private List<ScheduleTeacherResponse> afternoonList;

    private List<ScheduleTeacherResponse> eveningList;
}
