package com.example.onekids_project.request.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateTabAllSchedule {
    private List<CreateTabDayInWeek> createTabDayInWeek;
}
