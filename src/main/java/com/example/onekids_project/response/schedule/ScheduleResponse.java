package com.example.onekids_project.response.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleResponse {
    private Long idClass;
    private String className;
    private boolean morningSaturday;
    private boolean afternoonSaturday;
    private boolean eveningSaturday;
    private boolean sunday;
    private boolean approve;
    private  String scheduleTitle;
    List<ScheduleDayRespone> scheduleDayResponeList;
}
