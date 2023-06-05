package com.example.onekids_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {

    private LocalDate timeSchedule;

    private String timeMorning;

    private String contentMorning;

    private String timeAfternoon;

    private String contentAfternoon;

    private String timeEvening;

    private String contentEvening;


}
