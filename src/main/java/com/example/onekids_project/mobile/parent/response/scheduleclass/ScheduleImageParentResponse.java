package com.example.onekids_project.mobile.parent.response.scheduleclass;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ScheduleImageParentResponse {
    private String weekName;
    private List<String> pictureList;
}
