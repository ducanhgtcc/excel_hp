package com.example.onekids_project.mobile.teacher.response.scheduleclass;

import com.example.onekids_project.mobile.parent.response.scheduleclass.ScheduleParentResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ScheduleWeekTeacherResponse {
    private String title;
    private String date;

    private List<ScheduleTeacherResponse> morningList;

    private List<ScheduleTeacherResponse> afternoonList;

    private List<ScheduleTeacherResponse> eveningList;
}
