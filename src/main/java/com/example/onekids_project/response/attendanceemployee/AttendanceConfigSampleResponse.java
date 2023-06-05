package com.example.onekids_project.response.attendanceemployee;

import lombok.Data;

/**
 * date 2021-03-08 2:07 CH
 *
 * @author ADMIN
 */

@Data
public class AttendanceConfigSampleResponse {

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
