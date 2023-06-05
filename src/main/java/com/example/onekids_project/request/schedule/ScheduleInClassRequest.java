package com.example.onekids_project.request.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInClassRequest {
    private Long idClass;
    private String sessionDay;
    private String timeContent;
    private String contentSchedule;
}
