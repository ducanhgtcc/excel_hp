package com.example.onekids_project.mobile.plus.response.schedule;

import com.example.onekids_project.mobile.response.ScheduleStatusDay;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleClassWeekResponse extends IdResponse {

    String nameClass;

    boolean isSchedule;

    List<ScheduleStatusDay> statusDayList;
}
