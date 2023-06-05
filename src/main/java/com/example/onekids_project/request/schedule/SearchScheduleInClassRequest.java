package com.example.onekids_project.request.schedule;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SearchScheduleInClassRequest {
    private String timeSchedule;
    private Long idClass;
    //private String mondayOfWeek;
    private String isMonday;
}
