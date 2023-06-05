package com.example.onekids_project.request.employeeSalary;

import lombok.Data;

/**
 * date 2021-03-09 8:58 SA
 *
 * @author ADMIN
 */

@Data
public class AttendanceEmployeeConfigRequest {

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
