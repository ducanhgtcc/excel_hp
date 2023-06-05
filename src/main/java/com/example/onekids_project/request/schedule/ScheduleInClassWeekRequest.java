package com.example.onekids_project.request.schedule;

import com.example.onekids_project.response.schedule.ScheduleInClassResponse;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ScheduleInClassWeekRequest {
    private Long idSchedule;
    private LocalDate scheduleDate;
//    private String scheduleTitle;
    private Long idClass;
    private List<ScheduleInClassRequest> scheduleInClassResponseList;
}
