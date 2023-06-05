package com.example.onekids_project.response.school;

import lombok.Data;

/**
 * date 2021-03-06 3:52 CH
 *
 * @author ADMIN
 */
@Data
public class ConfigAttendanceEmployeeSchoolResponse {

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
