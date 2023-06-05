package com.example.onekids_project.response.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDayRespone {
    private Long idClass;
    private String className;
    private String sessionDay;
    /*private String timeMonday;
    private String contentMonday;*/
    private String monday;
    /*private String timeTuesday;
    private String contentTuesday;*/
    private String tuesday;
    /*private String timeWednesday;
    private String contentWednesday;*/
    private String wednesday;
    /*private String timeThursday;
    private String contentThursday;*/
    private String thursday;
    /*private String timeFriday;
    private String contentFriday;*/
    private String friday;
    /*private String timeSaturday;
    private String contentSaturday;*/
    private String saturday;
    /*private String timeSunday;
    private String contentSunday;*/
    private String sunday;
}
