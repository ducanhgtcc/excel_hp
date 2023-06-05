package com.example.onekids_project.request.schedule;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class SearchScheduleRequest {
    private String timeSchedule;
    private Long idGrade;
    private Long idClass;
}
