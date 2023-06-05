package com.example.onekids_project.mobile.parent.response.schoolconfig;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class AbsentConfig {
    private Integer number;

    private LocalTime time;
}
