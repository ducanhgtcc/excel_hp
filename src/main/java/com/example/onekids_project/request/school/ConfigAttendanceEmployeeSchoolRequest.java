package com.example.onekids_project.request.school;

import lombok.Getter;
import lombok.Setter;

/**
 * date 2021-03-06 4:18 CH
 *
 * @author ADMIN
 */
@Getter
@Setter
public class ConfigAttendanceEmployeeSchoolRequest {
    private boolean morning;

    private boolean afternoon;

    private boolean evening;

    private boolean saturdayMorning;

    private boolean saturdayAfternoon;

    private boolean saturdayEvening;

    private boolean sundayMorning;

    private boolean sundayAfternoon;

    private boolean sundayEvening;

    private boolean breakfast;

    private boolean lunch;

    private boolean dinner;
}
