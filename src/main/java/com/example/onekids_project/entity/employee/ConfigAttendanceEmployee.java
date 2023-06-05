package com.example.onekids_project.entity.employee;

import com.example.onekids_project.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * date 2021-03-06 2:13 CH
 *
 * @author ADMIN
 */

@Getter
@Setter
@Entity
@Table(name = "config_attendance_employee")
public class ConfigAttendanceEmployee extends BaseEntity<String> {

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_info_employee", nullable = false)
    private InfoEmployeeSchool infoEmployeeSchool;

}
