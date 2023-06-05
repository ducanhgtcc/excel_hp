package com.example.onekids_project.entity.school;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * date 2021-03-06 2:13 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
@Entity
@Table(name = "config_attendance_employee_school")
public class ConfigAttendanceEmployeeSchool extends BaseEntity<String> {

    private boolean morning = AppConstant.APP_TRUE;

    private boolean afternoon = AppConstant.APP_TRUE;

    private boolean evening;

    private boolean saturdayMorning = AppConstant.APP_TRUE;

    private boolean saturdayAfternoon = AppConstant.APP_TRUE;

    private boolean saturdayEvening;

    private boolean sundayMorning;

    private boolean sundayAfternoon;

    private boolean sundayEvening;

    private boolean breakfast;

    private boolean lunch = AppConstant.APP_TRUE;

    private boolean dinner;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_school", nullable = false)
    private School school;

}
