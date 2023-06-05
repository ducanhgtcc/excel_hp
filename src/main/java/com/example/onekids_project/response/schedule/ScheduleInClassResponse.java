package com.example.onekids_project.response.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInClassResponse {
    private Long idClass;
    private String className;
    private String sessionDay;
    private String timeContent;
    private String contentSchedule;
    private String timeSchedule;
}
