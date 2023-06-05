package com.example.onekids_project.response.schedule;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ScheduleInClassWeekResponse {
    private Long idSchedule;
    private LocalDate scheduleDate;
    private String scheduleTitle;
    private boolean isApproved;
    private Long idClass;
    private List<ScheduleInClassResponse> scheduleInClassResponseList;
}
