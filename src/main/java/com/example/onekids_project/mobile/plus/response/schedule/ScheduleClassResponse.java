package com.example.onekids_project.mobile.plus.response.schedule;

import com.example.onekids_project.mobile.response.FeatureClassResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleClassResponse {
    
    private FeatureClassResponse featureClassResponse;

    private boolean isSchedule;
}
