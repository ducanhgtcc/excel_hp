package com.example.onekids_project.mobile.parent.response.scheduleclass;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScheduleDateParentResponse {

    private String title;

    private List<ScheduleParentResponse> morningList;

    private List<ScheduleParentResponse> afternoonList;

    private List<ScheduleParentResponse> eveningList;

}
