package com.example.onekids_project.request.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMultiSchedule {
    private List<CreateTabAllSchedule> createTabAllSchedule;
    private List<String> weekSchedule;
    private List<Long> listIdClass;
    private  String scheduleTitle;
}
